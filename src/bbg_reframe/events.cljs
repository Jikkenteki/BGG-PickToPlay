(ns bbg-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [ajax.core :as ajax]
   [bbg-reframe.model.sort-filter :refer [sorting-fun rating-higher-than? with-number-of-players? and-filters is-playable-with-num-of-players playingtime-between? game-more-playable?]]
   [clojure.tools.reader.edn :refer [read-string]]
   [bbg-reframe.model.db :refer [read-db game-id collection-game->game game-votes]]
   [tubax.core :refer [xml->clj]]
   [bbg-reframe.model.localstorage :refer [set-item!]]
   [clojure.string :refer [split]]))


(def delay-between-fetch 1000)



(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            ;; db/default-db
            {:result nil
             :fields ["name"]
             :form {:sort-id "playable"
                    :take "10"
                    :higher-than "7.0"
                    :players "4"
                    :threshold "0.8"
                    :time-limit "180"}
             :games (read-db)
             :queue #{}
             :fetching #{}
             :fetches 0
             :error nil
             :cors-running false}))


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
 ::update-result
 (fn-traced [{:keys [db]} _]
            (let [sort-by  (get-in db [:form :sort-id])
                  sorting-fun (if (= sort-by "playable")
                                (game-more-playable? (read-string (get-in db [:form :players])))
                                (get sorting-fun (keyword (get-in db [:form :sort-id]))))
                  result (take (read-string (get-in db [:form :take]))
                               (sort sorting-fun
                                     (filter
                                      (and-filters
                                       (with-number-of-players?
                                         (read-string (get-in db [:form :players])))
                                       (rating-higher-than?
                                        (read-string (get-in db [:form :higher-than])))
                                       (playingtime-between?
                                        0 (read-string (get-in db [:form :time-limit])))
                                       (is-playable-with-num-of-players
                                        (get-in db [:form :players])
                                        (get-in db [:form :threshold])))
                                      (vals (get db :games)))))]
              {:db (assoc db :result result)
               :dispatch [::update-queue (map :id result)]})))

(re-frame/reg-event-fx
 ::update-form
 (fn-traced [{:keys [db]} [_ id val]]
            {:db (assoc-in db [:form id] val)
             :dispatch [::update-result]}))

(def cors-server-uri "https://guarded-wildwood-02993.herokuapp.com/")

(re-frame/reg-event-fx                             ;; note the trailing -fx
 ::fetch-collection                      ;; usage:  (dispatch [:handler-with-http])
 (fn-traced [{:keys [db] {:keys [cors-running]} :db} [_ user-name]]                    ;; the first param will be "world"
            (if cors-running
              {:db   (assoc db :loading true)   ;; causes the twirly-waiting-dialog to show??
               :http-xhrio {:method          :get
                            :uri             (str cors-server-uri "https://boardgamegeek.com/xmlapi/collection/" user-name)
                            :timeout         8000                                           ;; optional see API docs
                            :response-format (ajax/text-response-format)  ;; IMPORTANT!: You must provide this.
                            :on-success      [::success-fetch-collection]
                            :on-failure      [::bad-http-collection]}}
              {:db (assoc db
                          :error "CORS server not responding. Trying again in 2 seconds")
               :dispatch-later {:ms 2000
                                :dispatch [::fetch-collection user-name]}})))


(re-frame/reg-event-fx                             ;; note the trailing -fx
 ::fetch-game                      ;; usage:  (dispatch [:handler-with-http])
 (fn-traced [{:keys [db] {:keys [cors-running]} :db} [_ game-id]]                    ;; the first param will be "world"
            (if cors-running
              {:db   (assoc db :loading true)   ;; causes the twirly-waiting-dialog to show??
               :http-xhrio {:method          :get
                            :uri             (str cors-server-uri "https://boardgamegeek.com/xmlapi/boardgame/" game-id)
                            :timeout         8000                                           ;; optional see API docs
                            :response-format (ajax/text-response-format)  ;; IMPORTANT!: You must provide this.
                            :on-success      [::success-fetch-game]
                            :on-failure      [::bad-http-game]}}
              {})))



;; BAD REQUEST
;; core.cljs:200 Response:  
;; {:response "<error>
;;     <message>Rate limit exceeded.</message>                        
;;                           </error>", 
;;  :last-method GET, 
;;                         :last-error Too Many Requests [429], 
;;                           :failure 
;;                           :error, 
;;                           :status-text Too Many Requests, 
;;                           :status 429, 
;;                           :uri https://guarded-wildwood-02993.herokuapp.com/https://boardgamegeek.com/xmlapi/collection/ddmits, 
;;                           :debug-message Http response at 400 or 500 level, 
;;                           :last-error-code 6}

;; 
;; 
;; Checking CORS server
;; 
(re-frame/reg-event-fx                             ;; note the trailing -fx
 ::cors                      ;; usage:  (dispatch [:handler-with-http])
 (fn-traced [{:keys [db]} _]                    ;; the first param will be "world"
            {:db   (assoc db :cors-running false)   ;; causes the twirly-waiting-dialog to show??
             :http-xhrio {:method          :get
                          :uri             cors-server-uri
                          :timeout         8000                                           ;; optional see API docs
                          :response-format (ajax/text-response-format)  ;; IMPORTANT!: You must provide this.
                          :on-success      [::success-cors]
                          :on-failure      [::bad-cors]}}))

(re-frame/reg-event-fx
 ::success-cors
 (fn-traced [{:keys [db]} _]
            (println (str "CORS server at " cors-server-uri " up!"))
            {:db (assoc db :cors-running true)}))

(re-frame/reg-event-fx
 ::bad-cors
 (fn-traced [{:keys [db]} _]
            (println "CORS server down")
            {:db (assoc db :error "CORS server down")}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 

(re-frame/reg-event-fx
 ::success-fetch-collection
 (fn-traced [cofx [_ response]]
            (println "SUCCESS: collection fetched ")
            (let [collection (:content (xml->clj response))
                  games (map collection-game->game collection)
                  indexed-games (reduce
                                 #(assoc %1 (:id %2) %2)
                                 {} games)
                  _ (set-item! "ls-games" indexed-games)
        ;;  collection-to-be-fetched (drop-while #(item-exists? (ls-name %)) collection)
                  collection-to-be-fetched collection
                  _ (println (count collection-to-be-fetched))]
              {:dispatch [::update-result]
               :db (assoc (:db cofx)
                          :games indexed-games
                          :loading false)})))



(re-frame/reg-event-fx
 ::update-queue
;;  for some strange reason fn-traceddoes not compile
 (fn [{:keys [db] {:keys [queue fetching games]} :db} [_ results]]
   (let [fetched (into #{} (->> (vals games)
                                (remove #(nil? (:votes %)))
                                (map :id)))
         new-to-fetch (->> results
                           (remove #(fetched %))
                           (remove #(queue %))
                           (remove #(fetching %)))]
     {:db (assoc db
                 :queue (reduce #(conj %1 %2) queue new-to-fetch))
      :dispatch [::fetch-next-from-queue]})))

(re-frame/reg-event-fx
 ::success-fetch-game
 (fn-traced [{:keys [db] {:keys [fetches fetching]} :db} [_ response]]
            (let [game-received (->> response
                                     xml->clj
                                     :content
                                     first)
                  game-id (game-id game-received)
                  _  (println "SUCCESS" game-id)
                  new-db (assoc-in db [:games game-id :votes] (game-votes game-received))
                  _ (set-item! "ls-games" (:games new-db))]
              {:db (assoc new-db
                          :error nil
                          :fetching (disj fetching game-id)
                          :fetches (inc fetches))
               :fx [[:dispatch [::update-result]]
                    [:dispatch [::fetch-next-from-queue]]]})))

(re-frame/reg-event-fx
 ::bad-http-collection
 (fn-traced [{:keys [db]} [_ response]]
            (println "BAD REQUEST")
            (println "Response: " response)
            (cond
              (= 0 (:status response)) {:db (assoc db
                                                   :queue #{}
                                                   :fetching #{}
                                                   :error "CORS server is not responding"
                                                   :loading false
                                                   :cors-running false)}
              :else {:db (assoc db
                                :queue #{}
                                :fetching #{}
                                :error (:status-text response)
                                :loading false
                                :cors-running false)})))

(re-frame/reg-event-fx
 ::bad-http-game
 (fn-traced [{:keys [db] {:keys [queue fetching]} :db} [_ response]]
            (println "BAD REQUEST")
            (println "Response: " response)
            (if (= 0 (:status response))
              {:db (assoc db
                          :queue #{}
                          :fetching #{}
                          :error "CORS server is not responding"
                          :loading false
                          :cors-running false)}
              (let [uri (:uri response)
                    game-id (last (split uri \/))
                    _ (println (str (:status-text response) "Puting " game-id " back in the queue"))]
                {:db {assoc db
                      :queue (conj queue game-id)
                      :fetching (disj fetching game-id)}
                 :dispatch [::fetch-next-from-queue]}))))

(re-frame/reg-event-fx
 ::fetch-next-from-queue
 (fn-traced [{:keys [db] {:keys [queue fetching]} :db} _]
            (if (empty? queue)
              (if (empty? fetching)
                {:db (assoc db :loading false)}
                {})
              (let [fetch-now (first queue)
                    _ (println "fetch-next-from-queue: fetching " fetch-now)]
                {:db (assoc db
                            :queue (disj queue fetch-now)
                            :fetching (conj fetching fetch-now)
                            :loading true)
                 :dispatch-later {:ms (* (inc (count fetching)) delay-between-fetch)
                                  :dispatch [::fetch-game fetch-now]}}))))