(ns bbg-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [clojure.spec.alpha :as s]
   [clojure.string :refer [trim]]
   [re-frame.loggers :refer [console]]

   [bbg-reframe.model.localstorage :refer [set-item!]]
   [bbg-reframe.db :refer [default-db]]
   [bbg-reframe.spec.db-spec :as db-spec]))

(defn check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (console :error (s/explain-str  a-spec db))
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

;; now we create an interceptor using `after`
(def check-spec-interceptor (re-frame/after (partial check-and-throw ::db-spec/db)))


(re-frame/reg-event-db
 ::initialize-db
 [check-spec-interceptor]
 (fn-traced
  [_ _]
  default-db))

;; (re-frame/reg-event-db
;;  ::field
;;  (fn-traced[db [_ field e]]
;;    (let [_ (println e field)
;;          new-fields
;;          (if (some #(= field (:name %)) (:fields db))
;;            (filter #(not= field %) (:fields db))
;;            (conj (:fields db) field))]
;;      (assoc db :fields new-fields))))


(defn bbg-user-settings->local-store
  "Puts user and settings into localStorage"
  [db]
  (set-item! "bgg-user" (:user db))
  (set-item! "bgg-ui-settings" (:form db)))

;; Interceptor that saves the games from db to local-storage
(def ->bbg-user-settings->local-store (re-frame/after bbg-user-settings->local-store))

(re-frame/reg-event-fx
 ::update-form
 [check-spec-interceptor ->bbg-user-settings->local-store]
 (fn-traced [{:keys [db]} [_ id val]]
            {:db (assoc-in db [:form id] val)
             :dispatch [:bbg-reframe.network-events/update-result]}))

(re-frame/reg-event-fx
 ::update-user
 [check-spec-interceptor ->bbg-user-settings->local-store]
 (fn-traced [{:keys [db]} [_ val]]
            {:db (assoc db :user (trim val))}))



(re-frame/reg-event-fx
 ::update-games
 [check-spec-interceptor]
 (fn-traced [{:keys [db]} [_ val]]
            {:db (assoc db :games val)}))

(re-frame/reg-event-fx
 ::update-ui-settings
 [check-spec-interceptor]
 (fn-traced [{:keys [db]} [_ val]]
            {:db (assoc db :form val)}))

(re-frame/reg-event-db
 ::set-open-tab
 [check-spec-interceptor]
 (fn-traced [db [_ tab]]
            (assoc-in db [:ui :open-tab] (if (= (get-in db [:ui :open-tab]) tab)
                                           nil
                                           tab))))

;; routing
(comment
  ;; navigates to the route specified by the keyword
  (re-frame/dispatch [::navigate :home])
  (re-frame/dispatch [::navigate :fb])
  (re-frame/dispatch [::navigate :collections-view])
  ;
  )

(re-frame/reg-event-fx
 ::navigate
 (fn-traced [_ [_ handler]]
            {:navigate handler}))

;; (re-frame/reg-event-fx
;;  ::set-active-panel
;;  (fn-traced [{:keys [db]} [_ active-panel]]
;;             {:db (assoc db :active-panel active-panel)}))

(re-frame/reg-event-db
 ::set-route
 (fn-traced [db [_ route]]
            (assoc db :route route)))



