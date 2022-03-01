(ns bbg-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [bbg-reframe.model.localstorage :refer [set-item!]]
   [bbg-reframe.db :refer [default-db]]))


(re-frame/reg-event-db
 ::initialize-db
 (fn-traced
  [_ _]
  default-db))

;; (re-frame/reg-event-db
;;  ::field
;;  (fn-traced[db [_ field e]]
;;    (let [_ (println e field)
;;          new-fields
;;          (if (some #(= field %) (:fields db))
;;            (filter #(not= field %) (:fields db))
;;            (conj (:fields db) field))]
;;      (assoc db :fields new-fields))))



(re-frame/reg-event-fx
 ::update-form
 (fn-traced [{:keys [db]} [_ id val]]
            (set-item! "bgg-ui-settings" (assoc (:form db) id val))
            {:db (assoc-in db [:form id] val)
             :dispatch [:bbg-reframe.network-events/update-result]}))

(re-frame/reg-event-fx
 ::update-user
 (fn-traced [{:keys [db]} [_ val]]
            (let [_ (set-item! "bgg-user" val)]
              {:db (assoc db :user val)})))

(re-frame/reg-event-fx
 ::update-games
 (fn-traced [{:keys [db]} [_ val]]
            {:db (assoc db :games val)}))

(re-frame/reg-event-fx
 ::update-ui-settings
 (fn-traced [{:keys [db]} [_ val]]
            {:db (assoc db :form val)}))


(re-frame/reg-event-db
 ::toggle-sort-by-button-state
 (fn [db]
   (assoc-in db [:ui :sort-by-button-state] (not (get-in db [:ui :sort-by-button-state])))))
