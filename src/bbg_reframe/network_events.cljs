(ns bbg-reframe.network-events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [ajax.core :as ajax]
   [clojure.tools.reader.edn :refer [read-string]]
   [bbg-reframe.model.db :refer [game-id game-votes xml->game indexed-games]]
   [bbg-reframe.model.localstorage :refer [set-item!]]
   [bbg-reframe.model.sort-filter :refer [sorting-fun rating-higher-than? with-number-of-players? and-filters is-playable-with-num-of-players playingtime-between? game-more-playable?]]

   [clojure.string :refer [split]]
   [re-frame.loggers :refer [console]]))


(def delay-between-fetches 100)
(def cors-server-uri "https://guarded-wildwood-02993.herokuappa.com/")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 
;; 
;; Checking CORS server
;; 
(re-frame/reg-event-fx                             ;; note the trailing -fx
 ::cors-check                      ;; usage:  (dispatch [:handler-with-http])
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
            (console :debug (str "CORS server at " cors-server-uri " up!"))
            {:db (assoc db :cors-running true)}))

;; status 0 Request failed. <-- (no network)
;; status -1 Request timed out. <-- Wrong address
(re-frame/reg-event-fx
 ::bad-cors
 (fn-traced [{:keys [db]} [_ response]]
            (console :error "CORS server down")
            (console :debug "status: " (:status response))
            (console :debug "status-text: " (:status-text response))
            {:db (assoc db :error "CORS server down")
             :dispatch-later {:ms 3000
                              :dispatch [::reset-error]}}))

(re-frame/reg-event-db
 ::reset-error
 (fn-traced
  [db _]
  (assoc db :error nil)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; EVENT GRAPH
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; fetch-collection
;;   success-fetch-collection
;;     update-result
;;       update-queue (with all results as args; adds one in the queue)
;;         fetch-next-from-queue
;;           fetch-game
;;             success-fetch-game
;;               fetch-next-from-queue (not empty? queue)
;;               update-result (empty? queue)
;;             failure-fetch-game
;;
;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 
;;
;; Collection
;;
(re-frame/reg-event-fx                             ;; note the trailing -fx
 ::fetch-collection                      ;; usage:  (dispatch [:handler-with-http])
 (fn-traced [{:keys [db] {:keys [cors-running user]} :db} [_ _]]                    ;; the first param will be "world"
            ;; (if cors-running
            {:db   (assoc db
                          :loading true
                          :error nil
                          :fetches 0)
             :http-xhrio {:method          :get
                          :uri             (str cors-server-uri "https://boardgamegeek.com/xmlapi/collection/" user)
                          :timeout         8000                                           ;; optional see API docs
                          :response-format (ajax/text-response-format)  ;; IMPORTANT!: You must provide this.
                          :on-success      [::success-fetch-collection]
                          :on-failure      [::bad-http-collection]}}
              ;; {:db (assoc db
              ;;             :error "CORS server not responding. Trying again in 2 seconds")
              ;; ;;  :dispatch [::cors-check]
              ;;  :dispatch-later {:ms 2000
              ;;                   :dispatch [::fetch-collection user]}}
            ;; )
            ))

(re-frame/reg-event-fx
 ::success-fetch-collection
 (fn-traced
  [{:keys [db]} [_ response]]
  (if-let [indexed-games (indexed-games response)]
    (let [_ (console :debug "SUCCESS: collection fetched: " (count indexed-games) " games.")
          _ (set-item! "bgg-games" indexed-games)]
      {:dispatch [::update-result]
       :db (assoc db
                  :games indexed-games
                  :loading false)})
    (let [_ (console :debug (str "ERROR: " response))]
      {:db (assoc db
                  :error (str "Error reading collection. Invalid user?")
                  :loading false)}))))

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

(re-frame/reg-event-fx
 ::bad-http-collection
 (fn-traced [{:keys [db]} [_ response]]
            (console :debug "FAILURE: " response)
            (merge (cond
                     (= 0 (:status response))
                     {:db (assoc db
                                 :queue #{}
                                 :fetching #{}
                                 :error "CORS server is not responding"
                                 :loading false
                                 :cors-running false)}
                     :else
                     {:db (assoc db
                                 :queue #{}
                                 :fetching #{}
                                 :error (:status-text response)
                                 :loading false)})
                   {:dispatch-later {:ms 3000
                                     :dispatch [::reset-error]}})))
;;
;; Update form result
;;
(re-frame/reg-event-fx
 ::update-result
 (fn-traced
  [{:keys [db]} _]
  (let [;; _ (console :debug "update")
        sort-by  (get-in db [:form :sort-id])
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
    (merge {:db (assoc db :result result :error nil)}
           (if (:cors-running db)
             {:dispatch [::update-queue (map :id result)]}
             {})))))


(defn fetched-games-ids
  [games]
  (into #{} (->> (vals games)
                 (remove #(nil? (:votes %)))
                 (map :id))))

(defn new-to-fetch
  "Remove from results ids the ids of the fetched games, queue, and fetching"
  [results games queue fetching]
  (->> results
       (remove #((fetched-games-ids games) %))
       (remove #(queue %))
       (remove #(fetching %))))

(re-frame/reg-event-fx
 ::update-queue
 (fn-traced [{:keys [db] {:keys [queue fetching games]} :db} [_ results]]
            (let [new-to-fetch (new-to-fetch results games queue fetching)]
              {:db (assoc db
                          :queue (reduce #(conj %1 %2) queue
                                ;; do not queue all ids; just the first one
                                ;; the rest might not be needed
                                ;; especially if the results change rapidly
                                         (if (first new-to-fetch) [(first new-to-fetch)] [])))
               :dispatch [::fetch-next-from-queue]})))


;;
;; Handler for dispatching the fetch for the next game
;; either from the queue or from the rest of the unfetched games
;;
(defn-traced fetch-next-from-queue-handler [{:keys [db] {:keys [queue fetching games]} :db} _]
  (if (and (empty? queue) (seq fetching)) ;; non-empty fetching
    ;; nothing to do; there are still games being fetched
    {}
    ;; else
    ;;   queue is not empty
    ;;   or
    ;;   fetching is empty
    ;;
    ;; CASES:
    ;;   queue not empty ; fetching not empty
    ;;   queue not empty ; fetching empty
    ;;   queue empty     ; fetching empty
    (let [fetch-now (if (empty? queue)
                      (first
                      ;;  (remove 
                      ;;         (fn [id] ((union 
                      ;;                    (fetched-games-ids games) 
                      ;;                    queue 
                      ;;                    fetching) id)) 
                      ;;         (keys games)) 
                       (new-to-fetch (keys games) games queue fetching)
                      ;;  (->> (keys games)
                      ;;             (remove #((fetched-games-ids games) %))
                      ;;             (remove #(queue %))
                      ;;             (remove #(fetching %)))
) ;; will be nil if all fetched
                      (first queue)) ;; if fetching not empty, queue is not empty

          new-fetching (if fetch-now
                         (conj fetching fetch-now)
                         fetching)]
      (when fetch-now
        (console :debug
                 (if (and (empty? queue) (empty? fetching))
                   "New to fetch (background): "
                   "fetch-next-from-queue: fetching ")
                 fetch-now))
      (merge
       {:db (assoc db
                   :queue (disj queue fetch-now)
                   :fetching new-fetching
                   :loading (> (+ (count queue) (count fetching)) 0))}
       (if fetch-now
         {:dispatch-later
          {:ms (* (inc (count fetching)) delay-between-fetches)
           :dispatch [::fetch-game fetch-now]}}
         {})))))

(re-frame/reg-event-fx
 ::fetch-next-from-queue
 fetch-next-from-queue-handler)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;  Fetch Game
;;
(re-frame/reg-event-fx                             ;; note the trailing -fx
 ::fetch-game                      ;; usage:  (dispatch [:handler-with-http])
 (fn-traced [{:keys [db] {:keys [cors-running]} :db} [_ game-id]]                    ;; the first param will be "world"
            (if cors-running
              {;;  :db   (assoc db :loading true)
               :http-xhrio {:method          :get
                            :uri             (str cors-server-uri "https://boardgamegeek.com/xmlapi/boardgame/" game-id)
                            :timeout         8000                                           ;; optional see API docs
                            :response-format (ajax/text-response-format)  ;; IMPORTANT!: You must provide this.
                            :on-success      [::success-fetch-game]
                            :on-failure      [::failure-fetch-game]}}
              {:db (assoc db
                          :error "CORS server not responding. Trying again in 2 seconds")
               :dispatch [::cors-check]
               :dispatch-later {:ms 2000
                                :dispatch [::fetch-game game-id]}})))

;;
;; Handler for successfully fetched game
;;
(defn fetched-game-handler
  [{:keys [db] {:keys [fetches fetching queue]} :db} game-id game-votes]
  (let [_  (console :debug "SUCCESS" game-id)
        new-db (assoc-in db [:games game-id :votes] game-votes)
        _ (set-item! "bgg-games" (:games new-db))]
    {:db (assoc new-db
                :error nil
                :fetching (disj fetching game-id)
                :fetches (inc fetches))
     :fx (if (empty? queue)
           [[:dispatch [::update-result]]]
           [[:dispatch [::fetch-next-from-queue]]])}))

(defn-traced success-fetch-game-handler
  [cofx [_ response]]
  (let [game-received (xml->game response)]
    (fetched-game-handler cofx (game-id game-received) (game-votes game-received))))

(re-frame/reg-event-fx
 ::success-fetch-game
 success-fetch-game-handler)


(re-frame/reg-event-fx
 ::failure-fetch-game
 (fn-traced
  [{:keys [db] {:keys [queue fetching]} :db} [_ response]]
  (console :debug "BAD REQUEST")
  (console :debug "Response: " response)
  (cond (= 0 (:status response))
        {:db (assoc db
                    :queue #{}
                    :fetching #{}
                    :error "CORS server is not responding"
                    :loading false
                    :cors-running false)}
        (#{500 503} (:status response))
        ;; BGG throttles the requests now, which is to say that if you send requests too frequently, 
        ;; the server will give you 500 or 503 return codes, reporting that it is too busy.
        (let [uri (:uri response)
              game-id (last (split uri \/))
              _ (console :debug (str (:status-text response) " Puting " game-id " back in the queue"))]
          {:db {assoc db
                :queue (conj queue game-id)
                :fetching (disj fetching game-id)}
           :dispatch-later {:ms 3000
                            :dispatch [::fetch-next-from-queue]}})
        (= 404 (:status response))
        {:db (assoc db
                    :queue #{}
                    :fetching #{}
                    :error (str "Game not found or bad address!")
                    :loading false)
         :dispatch [::fetch-next-from-queue]}
        :else
        {:db (assoc db
                    :queue #{}
                    :fetching #{}
                    :error "Problem with BGG??"
                    :loading false)
         :dispatch [::fetch-next-from-queue]})))


