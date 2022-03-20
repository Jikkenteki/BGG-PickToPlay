(ns bbg-reframe.forms.forms
  (:require [re-frame.core :as re-frame]
            [clojure.spec.alpha :as spec]
            [clojure.test :refer [is]]
            [bbg-reframe.forms.subs :as form-subs]
            [bbg-reframe.forms.events :as form-events]
            [bbg-reframe.forms.utils :refer [find-key-value-in-map-list if-nil?->value]]))


(defn db-get-ref
  [db-path]
  (re-frame/subscribe [::form-events/get-value db-path]))

(defn db-set-value!
  [db-path value]
  (re-frame/dispatch [::form-events/set-value! db-path value]))

;;
;; functions for generic form inputs
;;
(defmulti dispatch (fn [type _ _] type))

(defmethod dispatch :default [type path _]
  (throw (js/Error. (str "No dispatch method for form input element of type: " type " for path: " path))))

(defmethod dispatch :text [_ path value]
  (re-frame/dispatch [::form-events/set-value! path value]))

(defmethod dispatch :password [_ path value]
  (re-frame/dispatch [::form-events/set-value! path value]))

(defmethod dispatch :checkbox [_ path _]
  (re-frame/dispatch [::form-events/update-value! path not]))

(defmulti input-element (fn [{:keys [type]}] type))

(defmethod input-element :default [{:keys [type path post-fn class placeholder]}]
  [:input {:class class
           :type (name type) :value @(db-get-ref path)
           :placeholder placeholder
           :on-change #(dispatch type path (post-fn (-> % .-target .-value)))}])

(defmethod input-element :checkbox [{:keys [type path post-fn]}]
  (let [checked @(db-get-ref path)]
    [:input.checkbox {:type (name type) :checked (if (nil? checked) false checked)
                      :on-change #(dispatch type path (post-fn (-> % .-target .-value)))}]))
(defn input
  [{:keys [label type path post-fn] :as params}]
  {:pre [(is (spec/valid?
              (spec/keys :req-un [label type path]) params))]}
  (let [post-fn (if-nil?->value post-fn identity)]
    [:div
     [:label label]
     [input-element {:type type :path path :post-fn post-fn}]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn string-list->map-list
  [options]
  (map (fn [el] {:value el}) options))

(defn is-list-of-maps?
  [list]
  (and (seq list) (map? (first list))))

(is-list-of-maps? [])

(defn dropdown-search
  "Creates a **dropdown search component** consisting of a button, a search text, and a select element.
   \n - db-path is the path in the db for setting and reading the information,
   \n - options is a list of maps for the options in the select element,
   \n - id-keyword is a keyword within the maps; the value is saved in the path,
   \n - display-keyword is a keyword within the maps; the value is shown in the options,
   \n - button-text-empty is the button text when nothing is selected,
   \n - input-placeholder is the placeholder for the search text box,
   \n - select-nothing-text is the text in the first (Nothing) option."
  [{:keys [db-path options id-keyword display-keyword button-text-empty input-placeholder select-nothing-text sort?] :as config}]
  {:pre [(is (spec/valid?
              (spec/keys :req-un [db-path options]) config))]}
  (let [[options id-keyword display-keyword]
        (if (or (empty? options) (is-list-of-maps? options))
          (cond
            (and (nil? id-keyword) (nil? display-keyword))
            (throw (js/Error (str "dropdown-search: both id-keyword and display-keyword are nil. Cannot work with a list of maps. Args: " config)))
            (nil? id-keyword) [options display-keyword display-keyword]
            (nil? display-keyword) [options id-keyword id-keyword]
            :else [options id-keyword display-keyword])
          (if (or id-keyword display-keyword)
            (throw (js/Error (str "dropdown-seach: id-keyword and display-keyword are ignored when you provide a list of values. Args: " config)))
            [(string-list->map-list options) :value :value]))
        button-text-empty (if-nil?->value button-text-empty "Click to select")
        input-placeholder (if-nil?->value input-placeholder "Type to filter options")
        select-nothing-text (if-nil?->value select-nothing-text "(no selection)")
        initial-value @(db-get-ref db-path)
        _ (db-set-value! (into [:dropdown-search :value] db-path)
                         (display-keyword (find-key-value-in-map-list options id-keyword initial-value)))
        value @(db-get-ref (into [:dropdown-search :value] db-path))
        button-text (if-nil?->value value button-text-empty)
        visible? (db-get-ref (into [:dropdown-search :visible] db-path))
        display-style {:display (if (if-nil?->value @visible? false) "block" "none")}
        select-options @(re-frame/subscribe [::form-subs/dropdown-select-options (into [:dropdown-search :search] db-path)
                                             options {:sort? sort? :by display-keyword}])
        style {:width "100%"}]

    [:div
     [:button.button.min-w-fit.px-2.ml-1 {:style style
                                          :on-click #(db-set-value! (into [:dropdown-search :visible] db-path)
                                                                    (not (if-nil?->value @visible? false)))} button-text]

     [:input.input-box.min-w-0.grow.h-full {:style (merge display-style style)
                                            :type :text
                                            :placeholder input-placeholder
                                            :value @(db-get-ref (into [:dropdown-search :search] db-path))
                                            :on-change #(db-set-value! (into [:dropdown-search :search] db-path) (-> % .-target .-value))}]

     [:select.select {:style (merge display-style style)
                      :size @(re-frame/subscribe [::form-subs/dropdown-select-size (into [:dropdown-search :search] db-path) options])
                      :value (if-nil?->value value "")
                      :on-change
                      (fn [e]
                        (let [selected-index (-> e .-target .-selectedIndex)
                       ;; first option is (Nothing to select)
                              selected-id (if (= 0 selected-index)
                                            nil
                                            (id-keyword (nth select-options (dec selected-index))))]
                          (db-set-value! (into [:dropdown-search :visible] db-path) false)                 ;; hide the search text and select elements
                          (db-set-value! (into [:dropdown-search :search] db-path) "")                     ;; clear the search text
                          (db-set-value! (into [:dropdown-search :value] db-path) (-> e .-target .-value)) ;; change the selected value
                          (db-set-value! db-path selected-id)))}                                           ;; change in db the id of the selected element
      [:option.option.bg-stone-800.text-neutral-200 {:value ""} select-nothing-text]
      (map (fn [m] [:option.option.bg-stone-800.text-neutral-200 {:key (id-keyword m) :id (id-keyword m) :value (display-keyword m)} (display-keyword m)])
           select-options)]]))




