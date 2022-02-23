(ns bbg-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [ajax.core :as ajax]
   [bbg-reframe.model.sort-filter :refer [sorting-fun rating-higher-than? with-number-of-players? and-filters is-playable-with-num-of-players playingtime-between?]]
   [clojure.tools.reader.edn :refer [read-string]]
   [bbg-reframe.model.db :refer [read-db game-id read-collection-from-file collection-game->game game-votes]]
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
                    :time-limit "120"}
             :games (read-db)}))


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
                            ;;  (vals (db :collection))
                             (vals (get db :games))
                             ))))]
     
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


;; (defn- ls-name [game]
;;   (str "resources/game" (game-id game) ".clj"))

(re-frame/reg-event-fx
 ::success-fetch-collection
 (fn [cofx [_ response]]
   (println "SUCCESS: collection fetched ")
  ;;  (spit "resources/collection.clj" (with-out-str (pprint (xml->clj response))))
   (let [collection (:content (xml->clj response))
         games (map collection-game->game collection)
         indexed-games (reduce
                #(assoc %1 (:id %2) %2)
                {} games)
        ;;  collection-to-be-fetched (drop-while #(item-exists? (ls-name %)) collection)
         collection-to-be-fetched collection
         _ (println (count collection-to-be-fetched))
        ;;  _ (println  (map game-id collection))
        ;;  _ (mapv #(println "id: " %) (map game-id collection))
         new-db (assoc (:db cofx) :games indexed-games)
         _ (spit "ls-games"indexed-games)
        ] 
   {:dispatch [::update-form :sort-id "rating"]
    :dispatch-later (mapv 
                     (fn [g-id delay] {:ms delay :dispatch [::fetch-game g-id]}) 
                     (map game-id collection-to-be-fetched)
                     (map #(* % 500) (range (count collection-to-be-fetched))))
    :db new-db})))

(comment 
(def collection ["1" "2" "3"])
{:dispatch-later (mapv 
 (fn [g-id delay] {:ms delay :dispatch [:eve g-id]})
 collection     (map #(* % 500) (range (count collection)))
 )}
  
  (map #(* % 500) (range (count collection)))

  (drop-while odd? [1 2 3 4 5])

)

(re-frame/reg-event-fx
 ::success-fetch-game
 (fn [cofx [_ response]]
   (let [game (->> response
                   xml->clj
                   :content
                   first)
         votes (game-votes game)
         game-id (game-id game)
         _  (println "SUCCESS" game-id)
        ;;  _ (spit (str "resources/game" game-id ".clj") (with-out-str (pprint game)))
         new-db (assoc-in (cofx :db) [:games game-id :votes] votes)
         _ (spit "ls-games" (:games new-db))]
   {:dispatch [::update-form :sort-id "rating"]
    :db new-db})))
 

(re-frame/reg-event-db
 ::bad-http-result
 (fn [db [_ response]]
   (println "BAD REQUEST")
   (println "Response: "response)
   db))
