(ns bbg-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [ajax.core :as ajax]
   [bbg-reframe.model.sort-filter :refer [sorting-fun rating-higher-than? with-number-of-players? and-filters is-playable-with-num-of-players playingtime-between? game-more-playable?]]
   [clojure.tools.reader.edn :refer [read-string]]
   [bbg-reframe.model.db :refer [read-db game-id collection-game->game game-votes]]
   [tubax.core :refer [xml->clj]]
   [bbg-reframe.model.localstorage :refer [spit]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            ;; db/default-db
            {:result nil
             :fields ["name"]
             :form {:sort-id "playable"
                    :take "10"
                    :higher-than "7"
                    :players "4"
                    :threshold "0.8"
                    :time-limit "120"}
             :games (read-db)
             :queue #{}
             :fetching #{}
             :delay 0
             :fetches 0}))


;; (re-frame/reg-event-db
;;  ::field
;;  (fn [db [_ field e]]
;;    (let [_ (println e field)
;;          new-fields
;;          (if (some #(= field %) (:fields db))
;;            (filter #(not= field %) (:fields db))
;;            (conj (:fields db) field))]
;;      (assoc db :fields new-fields))))

(re-frame/reg-event-fx
 ::update-result
 (fn [{:keys [db]} _]
   (let [sort-by  (get-in db [:form :sort-id])
         sorting-fun (if (= sort-by "playable")
                       (game-more-playable? (read-string (get-in db [:form :players])))
                       (get sorting-fun (keyword (get-in db [:form :sort-id]))))
        ;; sorting-fun (get sorting-fun (keyword (get-in db [:form :sort-id])))
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
 (fn [{:keys [db]} [_ id val]]
   {:db (assoc-in db [:form id] val)
    :dispatch [::update-result]}))


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


;; (re-frame/reg-event-fx
;;  ::success-fetch-collection
;;  (fn [cofx [_ response]]
;;    (println "SUCCESS: collection fetched ")
;;    (let [collection (:content (xml->clj response))
;;          games (map collection-game->game collection)
;;          indexed-games (reduce
;;                         #(assoc %1 (:id %2) %2)
;;                         {} games)
;;          _ (spit "ls-games" indexed-games)
;;         ;;  collection-to-be-fetched (drop-while #(item-exists? (ls-name %)) collection)
;;          collection-to-be-fetched collection
;;          _ (println (count collection-to-be-fetched))
;;          new-db (assoc (:db cofx) :games indexed-games)]
;;      {:dispatch [::update-result]
;;       :dispatch-later (mapv
;;                        (fn [g-id delay] {:ms delay :dispatch [::fetch-game g-id]})
;;                        (map game-id collection-to-be-fetched)
;;                        (map #(* % 500) (range (count collection-to-be-fetched))))
;;       :db new-db})))

(re-frame/reg-event-fx
 ::success-fetch-collection
 (fn [cofx [_ response]]
   (println "SUCCESS: collection fetched ")
   (let [collection (:content (xml->clj response))
         games (map collection-game->game collection)
         indexed-games (reduce
                        #(assoc %1 (:id %2) %2)
                        {} games)
         _ (spit "ls-games" indexed-games)
        ;;  collection-to-be-fetched (drop-while #(item-exists? (ls-name %)) collection)
         collection-to-be-fetched collection
         _ (println (count collection-to-be-fetched))
         new-db (assoc (:db cofx) :games indexed-games)]
     {:dispatch [::update-result]
      ;; :dispatch-later (mapv
      ;;                  (fn [g-id delay] {:ms delay :dispatch [::fetch-game g-id]})
      ;;                  (map game-id collection-to-be-fetched)
      ;;                  (map #(* % 500) (range (count collection-to-be-fetched))))
      :db new-db})))


(comment
  (def collection ["1" "2" "3"])
  {:dispatch-later (mapv
                    (fn [g-id delay] {:ms delay :dispatch [:eve g-id]})
                    collection     (map #(* % 500) (range (count collection))))}

  (map #(* % 500) (range (count collection)))

  (drop-while odd? [1 2 3 4 5]))

;; (re-frame/reg-event-fx
;;  ::success-fetch-game
;;  (fn [cofx [_ response]]
;;    (let [game (->> response
;;                    xml->clj
;;                    :content
;;                    first)
;;          votes (game-votes game)
;;          game-id (game-id game)
;;         ;;  _  (println "SUCCESS" game-id)
;;         ;;  _ (spit (str "resources/game" game-id ".clj") (with-out-str (pprint game)))
;;          new-db (assoc-in (cofx :db) [:games game-id :votes] votes)
;;          _ (spit "ls-games" (:games new-db))]
;;      {:dispatch [::update-result]
;;       :db new-db})))


(re-frame/reg-event-db
 ::bad-http-result
 (fn [db [_ response]]
   (println "BAD REQUEST")
   (println "Response: " response)
   db))

(def delay-between-fetch 100)

(re-frame/reg-event-fx
 ::update-queue
 (fn [{:keys [db] {:keys [queue fetching games delay]} :db} [_ results]]
   (let [fetched (into #{} (->> (vals games)
                                (remove #(nil? (:votes %)))
                                (map :id)))
         new-to-fetch (->> results
                           (remove #(fetched %))
                           (remove #(queue %))
                           (remove #(fetching %)))
         new-queue (reduce #(conj %1 %2) queue new-to-fetch)]
     {:db (assoc db
                 :queue new-queue
                 :delay (+ delay (* delay-between-fetch (count new-queue))))
      :dispatch [::fetch-next-from-queue]})))

;; (re-frame/reg-event-fx
;;  ::fetch-game-mock
;;  (fn [{:keys [db] {:keys [queue fetches fetching]} :db} [_ id]]
;;    (let [new-queue (disj queue id)
;;          fetch-now (first new-queue)
;;          fetching' (if fetch-now (conj fetching fetch-now) fetching)
;;          new-queue' (disj new-queue fetch-now)]
;;      (println "Fetching" id)
;;      (merge {:db (assoc db
;;                         :queue new-queue'
;;                         :fetching fetching'
;;                         :delay  (* 1000 (count new-queue))
;;                         :fetches (inc fetches))}
;;             (if fetch-now
;;               {:dispatch-later {:ms (* (count fetching) delay-between-fetch)
;;                                 :dispatch [::fetch-game fetch-now]}}
;;               {})))))

(re-frame/reg-event-fx
 ::success-fetch-game
 (fn [{:keys [db] {:keys [fetches fetching]} :db} [_ response]]
   (let [game-received (->> response
                            xml->clj
                            :content
                            first)
         game-id (game-id game-received)
         _  (println "SUCCESS" game-id)
         new-db (assoc-in db [:games game-id :votes] (game-votes game-received))
         _ (spit "ls-games" (:games new-db))]
     {:db (assoc new-db
                 :fetching (disj fetching game-id)
                 :fetches (inc fetches))
      :fx [[:dispatch [::update-result]]
           [:dispatch [::fetch-next-from-queue]]]})))

(re-frame/reg-event-fx
 ::fetch-next-from-queue
 (fn [{:keys [db] {:keys [queue fetching]} :db} _]
   (if (empty? queue)
     {}
     (let [fetch-now (first queue)]
       {:db (assoc db
                   :queue (disj queue fetch-now)
                   :fetching (conj fetching fetch-now))
        :fx [(when fetch-now [:dispatch-later {:ms (* (inc (count fetching)) delay-between-fetch)
                                               :dispatch [::fetch-game fetch-now]}])]}))))