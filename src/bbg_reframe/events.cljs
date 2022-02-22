(ns bbg-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [ajax.core :as ajax]
   [bbg-reframe.model.sort-filter :refer [sorting-fun rating-higher-than? with-number-of-players? and-filters is-playable-with-num-of-players playingtime-between?]]
   [clojure.tools.reader.edn :refer [read-string]]
   [bbg-reframe.model.db :refer [read-db]]
   [tubax.core :refer [xml->clj]]
   [cljs.pprint :refer [pprint]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            ;; db/default-db
            {:collection (read-db)
             :result nil
             :fields ["name"]
             :form {:sort-id "rating"
                    :take "10"
                    :higher-than "7"
                    :players "4"
                    :threshold "0.8"
                    :time-limit "120"}}))


(re-frame/reg-event-db
 ::field
 (fn [db [_ field e]]
   (let [_ (println e field)
         new-fields
         (if (some #(= field %) (:fields db))
           (filter #(not= field %) (:fields db))
           (conj (:fields db) field))]
     (assoc db :fields new-fields))))

(re-frame/reg-event-db
 ::update-form
 (fn [db [_ id val]]
   (let [updated-db (assoc-in db [:form id] val)
         new-db
         (assoc updated-db
                :result
                (take (read-string (get-in updated-db [:form :take]))
                      (sort (get sorting-fun (keyword (get-in updated-db [:form :sort-id])))
                            (filter
                             (and-filters
                              (with-number-of-players?
                                (read-string (get-in updated-db [:form :players])))
                              (rating-higher-than?
                               (read-string (get-in updated-db [:form :higher-than])))
                              (playingtime-between?
                               0 (read-string (get-in updated-db [:form :time-limit])))
                              (is-playable-with-num-of-players
                               (get-in updated-db [:form :players])
                               (get-in updated-db [:form :threshold])))
                             (vals (db :collection))))))]
     new-db)))


(re-frame/reg-event-fx                             ;; note the trailing -fx
  ::handler-with-http                      ;; usage:  (dispatch [:handler-with-http])
  (fn [{:keys [db]} _]                    ;; the first param will be "world"
    {:db   (assoc db :show-twirly true)   ;; causes the twirly-waiting-dialog to show??
     :http-xhrio {:method          :get
                  :uri             "https://boardgamegeek.com/xmlapi2/plays?username=ddmits&id=167791"
                  :timeout         8000                                           ;; optional see API docs
                  :response-format (ajax/text-response-format)  ;; IMPORTANT!: You must provide this.
                  :on-success      [::good-http-result]
                  :on-failure      [::bad-http-result]}}))

(re-frame/reg-event-db
 ::good-http-result
 (fn [db [_ response]]
   (println "SUCCESS")
   (pprint (xml->clj response))
   db))

(re-frame/reg-event-db
 ::bad-http-result
 (fn [db [_ response]]
   (println "BAD")
   (println response)
   db))
