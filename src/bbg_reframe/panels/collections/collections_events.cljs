(ns bbg-reframe.panels.collections.collections-events
  (:require [bbg-reframe.firebase.firebase-events :as firebase-events]
            [bbg-reframe.forms.forms :refer [db-get-ref db-set-value!]]
            [bbg-reframe.localstorage.localstorage-events :refer [->fb-collections->local-store]]
            [bbg-reframe.model.collection :refer [collection-name-exists?
                                                  make-collection]]
            [bbg-reframe.network-events :as network-events]
            [bbg-reframe.panels.home.components.search-comp-events :as search-comp-events]
            [day8.re-frame.tracing :refer-macros [fn-traced] :refer [defn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [re-frame.core :as re-frame]))

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
;; (reg-cofx               ;; registration function
;;  :collections           ;; what cofx-id are we registering
;;  (fn [coeffects _]      ;; second parameter not used in this caselog
;;    (assoc coeffects :collections @(re-frame/subscribe [::collections-subs/collections-auth]))))   ;; add :collections key, with value

;; (reg-cofx               ;; registration function
;;  :uid           ;; what cofx-id are we registering
;;  (fn [coeffects _]      ;; second parameter not used in this case
;;    (assoc coeffects :uid (fb-reframe/get-current-user-uid))))   ;; add :collections key, with value


;; handler for new collection
;; 
;; If the user is not logged-in there is an error message
;; Otherwise the new collection name is checked for uniqueness
;; If it is unique it is saved locally and pushed to fb.
;;
(defn-traced new-collection-handler
  [{:keys [db]} [_ form-path]]
  (if (nil? (:uid db))
    {:dispatch [::network-events/set-error "Login to save collections"]}
    (let [new-collection-name (:new-collection (get-in db form-path))
          data (make-collection new-collection-name)]
      (if (not (collection-name-exists? (:collections db) new-collection-name))
        {:fx
         [[::fb-reframe/firebase-push
           {:path ["users" (:uid db) "collections"]
            :data data
            :success #(re-frame/dispatch [::saved-collection new-collection-name])
            :key-path [:firebase :new-collection-id]}]
          [:dispatch [::add-collection-to-db {:form-path form-path :data data}]]]}
        {:dispatch [::network-events/set-error "Collection with this name already exists!"]}))))

;;
;; event for adding a new collection
(re-frame/reg-event-fx
 ::new-collection
;;  [;; (inject-cofx :collections) 
;;   (inject-cofx :uid)]
 new-collection-handler)


(defn-traced delete-collection-handler
  [{:keys [db]} [_ id]]
  (println (str "Delete collection with id: " id))
  {:db (update-in db [:collections] dissoc id)
   :fx [[:dispatch [::firebase-events/fb-set {:path ["collections" (name id)] :data nil}]]
        [:navigate [:collections-view]]]})

;;
;; event for deleting a new collection
(re-frame/reg-event-fx
 ::delete-collection
 [->fb-collections->local-store]
 delete-collection-handler)


(defn-traced edit-collection-name-handle
  [{:keys [db]} [_ [id form-path]]]
  (let [new-name (get-in db form-path)]
    (if (= new-name (get-in db [:collections id :name]))
      {}
      (if (not (collection-name-exists? (:collections db) new-name))
        {:db (assoc-in db [:collections id :name] new-name)
         :dispatch [::firebase-events/fb-set
                    {:path ["collections" (name id) "name"] :data new-name}]}
        {:dispatch [::network-events/set-error "Collection with this name already exists!"]}))))

;;
;; event for editing a new collection
(re-frame/reg-event-fx
 ::edit-collection-name
 [->fb-collections->local-store]
 edit-collection-name-handle)

(defn- get-new-collection-key [db]
  (keyword (get-in db [:firebase :new-collection-id])))

(re-frame/reg-event-db
 ::add-collection-to-db
 [->fb-collections->local-store]
 (fn-traced
  [db [_ {:keys [form-path data]}]]
  (-> db
      (assoc-in form-path {}) ;; clear the input form
      (assoc-in [:collections (get-new-collection-key db)] data))))

(re-frame/reg-event-db
 ::saved-collection
 (fn-traced [_ [_ name]]
            (println (str "Successfully pushed a collection in fb:" name))))


(re-frame/reg-event-fx
 ::add-game-to-collection
 (fn-traced
  [{:keys [db]} [_ [id game-id]]]
  (println "ADD GAME " id game-id)
  {:db (assoc-in db [:collections id :games (keyword game-id)] true)
   :fx [[:dispatch [::firebase-events/fb-set
                    {:path ["collections" (name id) "games" game-id] :data true}]]
        [:dispatch [::search-comp-events/reset-search]]]}))


(comment

  ;; this is performed by the UI
  (db-set-value! [:form :create-collection :new-collection] "ac")
  ;; the ui dispatches
  (re-frame/dispatch [::new-collection [:form :create-collection]])

  ;; edit name 
  @(db-get-ref [])
  (def new-key (get-new-collection-key @(db-get-ref [])))
  new-key

  (re-frame/dispatch [::edit-collection-name new-key "new ac2 upd 2"])


  ;
  )