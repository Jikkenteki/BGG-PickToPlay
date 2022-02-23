(ns bbg-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [ajax.core :as ajax]
   [bbg-reframe.model.sort-filter :refer [sorting-fun rating-higher-than? with-number-of-players? and-filters is-playable-with-num-of-players playingtime-between?]]
   [clojure.tools.reader.edn :refer [read-string]]
   [bbg-reframe.model.db :refer [read-db game-id read-collection-from-file]]
   [tubax.core :refer [xml->clj]]
   [cljs.pprint :refer [pprint]]
   [bbg-reframe.model.localstorage :refer [spit item-exists?]]))

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

(re-frame/reg-event-fx                             ;; note the trailing -fx
  ::fetch-collection                      ;; usage:  (dispatch [:handler-with-http])
  (fn [{:keys [db]} [_ user-name]]                    ;; the first param will be "world"
    {:db   (assoc db :show-twirly true)   ;; causes the twirly-waiting-dialog to show??
     :http-xhrio {:method          :get
                  :uri             (str "https://boardgamegeek.com/xmlapi/collection/" user-name)
                  :timeout         8000                                           ;; optional see API docs
                  :response-format (ajax/text-response-format)  ;; IMPORTANT!: You must provide this.
                  :on-success      [::success-fetch-collection]
                  :on-failure      [::bad-http-result]}}))

(re-frame/reg-event-fx                             ;; note the trailing -fx
  ::fetch-game                      ;; usage:  (dispatch [:handler-with-http])
  (fn [{:keys [db]} [_ game-id]]                    ;; the first param will be "world"
    {:db   (assoc db :show-twirly true)   ;; causes the twirly-waiting-dialog to show??
     :http-xhrio {:method          :get
                  :uri             (str "http://0.0.0.0:8080/https://boardgamegeek.com/xmlapi/boardgame/" game-id)
                  :timeout         8000                                           ;; optional see API docs
                  :response-format (ajax/text-response-format)  ;; IMPORTANT!: You must provide this.
                  :on-success      [::success-fetch-game]
                  :on-failure      [::bad-http-result]}}))


(re-frame/reg-event-db
 ::good-http-result
 (fn [db [_ response]]
   (println "SUCCESS")
   (pprint (xml->clj response))
   db))

;; change to fx and use dispatch later to add all games

(re-frame/reg-event-fx
 ::success-fetch-collection
 (fn [_ [_ response]]
   (println "SUCCESS")
   (spit "resources/collection.clj" (with-out-str (pprint (xml->clj response))))
   (let [collection (drop-while item-exists? (read-collection-from-file))
         _ (println (count collection))
        ;;  _ (println  (map game-id collection))
        ;;  _ (mapv #(println "id: " %) (map game-id collection))
         event {:dispatch-later (mapv  
                                 (fn [g-id] {:ms 1000 :dispatch [::fetch-game g-id]}) 
                                 collection)}] 
   event)))

(comment 
(def collection ["1" "2" "3"])
{:dispatch-later (mapv 
 (fn [g-id] {:ms 1000 :dispatch [:evfetch-gameent-id g-id]})
 collection     
 )}
  
  (drop-while odd? [1 2 3 4 5])

)

(re-frame/reg-event-db
 ::success-fetch-game
 (fn [db [_ response]]
   (let [_  (println "SUCCESS")
    game (->> response
        xml->clj
        :content
        first)
         game-id (game-id game)
   _ (spit (str "resources/game" game-id ".clj") (with-out-str (pprint game)))]
   db)))
;; => nil

   

(re-frame/reg-event-db
 ::bad-http-result
 (fn [db [_ response]]
   (println "BAD")
   (println response)
   db))
