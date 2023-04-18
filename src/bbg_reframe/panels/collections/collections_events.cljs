(ns bbg-reframe.panels.collections.collections-events
  (:require [bbg-reframe.forms.forms :refer [db-set-value!]]
            [bbg-reframe.localstorage.localstorage-events :refer [->fb-collections->local-store]]
            [bbg-reframe.network-events :as events]
            [bbg-reframe.panels.collections.collections-subs :as collections-subs]
            [bbg-reframe.panels.login.login-events :as login-events]
            [day8.re-frame-10x.inlined-deps.re-frame.v1v1v2.re-frame.core :refer [inject-cofx reg-cofx]]
            [day8.re-frame.tracing :refer-macros [fn-traced] :refer [defn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [re-frame.core :as re-frame]))

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn get-collection-names [collections]
  (if (nil? collections)
    []
    (reduce-kv (fn [m _ v] (conj m (:name v))) [] collections)))

;;
;; reg-cofx
;;
;; This function is also part of the re-frame API.
;; It allows you to associate a cofx-id (like :now or :local-store) with 
;; a handler function that injects the right key/value pair.
;; The function you register will be passed two arguments:
;; a :coeffects map (to which it should add a key/value pair), and
;; optionally, the additional value supplied to inject-cofx
;; and it is expected to return a modified :coeffects map.
;;
;; https://github.com/day8/re-frame/blob/master/docs/Coeffects.md
;;
(reg-cofx               ;; registration function
 :collections           ;; what cofx-id are we registering
 (fn [coeffects _]      ;; second parameter not used in this case
   (assoc coeffects :collections @(re-frame/subscribe [::collections-subs/collections-auth]))))   ;; add :collections key, with value

(reg-cofx               ;; registration function
 :uid           ;; what cofx-id are we registering
 (fn [coeffects _]      ;; second parameter not used in this case
   (assoc coeffects :uid (fb-reframe/get-current-user-uid))))   ;; add :collections key, with value

;; handler for new collection
;; in the coeffects we have 
;; the db 
;; the collections
;; the uid of the logged in user
;; 
;; If the user is not logged-in there is an error message
;; Otherwise the new collection name is checked for uniqueness
;; If it is unique it is saved locally and pushed to fb.
;;
(defn-traced new-collection-handler
  [{:keys [db uid]} [_ form-path]]
  (if (nil? uid)
    {:dispatch [::events/set-error "Login to save collections"]}
    (let [collections (:collections db)
          new-collection-name (:new-collection (get-in db form-path))
          data {:name new-collection-name}
          collection-names (get-collection-names collections)]
      (if (not (in? collection-names new-collection-name))
        {:fx
         [[::fb-reframe/firebase-push
           {:path ["users" uid "collections"]
            :data data
            :success #(re-frame/dispatch [::saved-collection new-collection-name])
            :key-path [:firebase :new-collection-id]}]
          [:dispatch [::update-db {:form-path form-path :data data}]]]}
        {:dispatch [::events/set-error "Collection with this name already exists!"]}))))

;;
;; event for adding a new collection
(re-frame/reg-event-fx
 ::new-collection
 [;; (inject-cofx :collections) 
  (inject-cofx :uid)]
 new-collection-handler)

(defn get-collection-key [db]
  (keyword (get-in db [:firebase :new-collection-id])))

(re-frame/reg-event-fx
 ::update-db
 [->fb-collections->local-store]
 (fn-traced [{:keys [db]} [_ {:keys [form-path data]}]]
            (let [key (get-collection-key db)]
              {:db (-> db
                       (assoc-in form-path {}) ;; clear the input form
                      ;;  (assoc :collections (conj collections {key data}))
                       (assoc-in [:collections key] data)
                       )})))

(re-frame/reg-event-db
 ::saved-collection
 (fn-traced [_ [_ name]]
            (println (str "Successfully pushed a collection in fb:" name))))

(comment
  (re-frame/dispatch [::login-events/sign-in "dranidis@gmail.com" "password"])
  (re-frame/dispatch [::login-events/sign-out])

  ;; this is performed by the UI
  (db-set-value! [:form :create-collection :new-collection] "ac")
  ;; the ui dispatches
  (re-frame/dispatch [::new-collection [:form :create-collection]])

  
  (get-collection-names {:-NTK-cZZcAJzjdrG6ifV {:name "ac"}})
  (get-collection-names {:-NTK-cZZcAJzjdrG6ifV {:name "ac"}
                         :af {:name "ab"}})
  ;
  )