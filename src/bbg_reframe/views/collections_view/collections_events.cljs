(ns bbg-reframe.views.collections-view.collections-events
  (:require  [bbg-reframe.views.collections-view.collections-subs :as collections-subs]
   [bbg-reframe.forms.forms :refer [db-get-ref]]
            [bbg-reframe.forms.events :as form-events]
            [bbg-reframe.firebase.events :as fb-events]
            [bbg-reframe.network-events :as events]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [re-frame-firebase-nine.firebase-database :refer [on-value push-value!]]
            [bbg-reframe.views.login-view.login-events :as login-events]
            [re-frame.core :as re-frame]))

;; helpers

(def collections-subpath "collections")

(defn collections-path
  "Returns the path to collections including the user id.
   If not authenticated returns nil."
  []
  (let [uId (fb-reframe/get-current-user-uid)]
    (if uId
      ["users" uId collections-subpath]
      nil)))

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn get-collection-names [collections]
  (reduce-kv (fn [m _ v] (conj m (:name v))) [] collections))

  ;;
  ;; subscribe to collections
  ;; 
;; (re-frame/reg-sub
;;  ::collections
;;  (fn []
;;    (re-frame/subscribe [::fb-reframe/on-value (collections-path)]))
;;  (fn [collections]
;;    (js/console.log "SUB collections " (collections-path))
;;    (if (nil? (collections-path))
;;      {}
;;      collections)))


(defn push-new-collection [name]
  (re-frame/dispatch-sync [::fb-events/fb-push {:path collections-subpath
                                                :data {:name name}
                                                :key-path [:firebase :new-collection-id]}]))

(defn push-new-collection! [name]
  (push-value! (collections-path) {:name name}))

;; (defn add-if-not-exists [name]
;;   (if (exists-collection-name name)
;;     nil
;;     (let [_ (db-set-value! [:firebase :new-collection-id] nil)
;;           _ (push-new-collection! name)]
;;       @(db-get-ref [:firebase :new-collection-id]))))


(defn get-collections []
  @(re-frame/subscribe [::collections-subs/collections-auth]))

(defn exists-collection-name [name]
  (in? (get-collection-names (get-collections)) name))

(defn add-if-not-exists
  "Creates a new collection and returns the key created by firebase.
   If the collection name already exists it does not create a new collection and returns nil."
  [name]
  (if (or (nil? (collections-path)) (exists-collection-name name))
    nil
    (push-new-collection! name)))

(re-frame/reg-event-fx
 ::new-collection
 (fn-traced [_ [_ form-path]]
            (let [user-id (fb-reframe/get-current-user-uid)]
              (if (nil? user-id)
                {:dispatch [::events/set-error "Login to save collections"]}
                (if (not (nil? (add-if-not-exists (:new-collection @(db-get-ref form-path)))))
                  {:dispatch [::form-events/set-value! form-path {}]}
                  {:dispatch [::events/set-error "Collection with this name already exists!"]})))))

(comment
  (collections-path)

  (nil? (collections-path))
  (re-frame/dispatch [::login-events/sign-in "dranidis@gmail.com" "password"])
  (re-frame/dispatch [::login-events/sign-out])

  (get-collection-names @(re-frame/subscribe [::collections]))

  (get-collection-names (get-collections))

  (add-if-not-exists "a collection's name 1")
  (add-if-not-exists "collection name")
  (exists-collection-name "collection name")
  (get-collections)
  ;; @(re-frame/subscribe [::collections])
  ;; @(re-frame/subscribe [::collections-auth])

  (on-value (collections-path) (fn [v] (js/console.log (get-collection-names v))))

  (println (str "Collections: " (get-collections)))
  ;
  )