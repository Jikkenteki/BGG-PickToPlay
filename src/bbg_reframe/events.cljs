(ns bbg-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [ajax.core :as ajax]
   [bbg-reframe.model.sort-filter :refer [sorting-fun rating-higher-than? with-number-of-players? and-filters is-playable-with-num-of-players playingtime-between?]]
   [clojure.tools.reader.edn :refer [read-string]]
   [clojure.set :refer [union]]
   [bbg-reframe.model.db :refer [read-db game-id collection-game->game game-votes]]
   [tubax.core :refer [xml->clj]]
   [bbg-reframe.model.localstorage :refer [spit]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            ;; db/default-db
            {:result nil
             :fields ["name"]
             :form {:sort-id "rating"
                    :take "10"
                    :higher-than "7"
                    :players "4"
                    :threshold "0.8"
                    :time-limit "120"}
             :games (read-db)
             :queue #{}
             :loaded #{}
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
   (let [result (take (read-string (get-in db [:form :take]))
                      (sort (get sorting-fun (keyword (get-in db [:form :sort-id])))
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
      :dispatch-later (mapv
                       (fn [g-id delay] {:ms delay :dispatch [::fetch-game g-id]})
                       (map game-id collection-to-be-fetched)
                       (map #(* % 500) (range (count collection-to-be-fetched))))
      :db new-db})))

(comment
  (def collection ["1" "2" "3"])
  {:dispatch-later (mapv
                    (fn [g-id delay] {:ms delay :dispatch [:eve g-id]})
                    collection     (map #(* % 500) (range (count collection))))}

  (map #(* % 500) (range (count collection)))

  (drop-while odd? [1 2 3 4 5]))

(re-frame/reg-event-fx
 ::success-fetch-game
 (fn [cofx [_ response]]
   (let [game (->> response
                   xml->clj
                   :content
                   first)
         votes (game-votes game)
         game-id (game-id game)
        ;;  _  (println "SUCCESS" game-id)
        ;;  _ (spit (str "resources/game" game-id ".clj") (with-out-str (pprint game)))
         new-db (assoc-in (cofx :db) [:games game-id :votes] votes)
         _ (spit "ls-games" (:games new-db))]
     {:dispatch [::update-result]
      :db new-db})))


(re-frame/reg-event-db
 ::bad-http-result
 (fn [db [_ response]]
   (println "BAD REQUEST")
   (println "Response: " response)
   db))

(re-frame/reg-event-fx
 ::update-queue
 (fn [{:keys [db] {:keys [queue loaded delay]} :db} [_ results]]
   (let [delay-between-fetch 1000
         to-fetch (drop-while #((union loaded queue) %) results)
         new-queue (reduce #(conj %1 %2) queue to-fetch)]
     {:db (assoc db :queue new-queue :delay (+ delay (* delay-between-fetch (count new-queue))))
      :dispatch-later (mapv
                       (fn [g-id delay] {:ms delay :dispatch [::fetch-game-mock g-id]})
                       to-fetch
                       (map #(+ (* % delay-between-fetch) (* 1000 (count queue))) (range (count to-fetch))))})))

(re-frame/reg-event-fx
 ::fetch-game-mock
 (fn [{:keys [db] {:keys [queue loaded fetches]} :db} [_ id]]
   (let [new-queue (disj queue id)]
     (println "Fetching" id)
     {:db (assoc db
                 :queue new-queue
                 :loaded (conj loaded id)
                 :delay  (* 1000 (count new-queue))
                 :fetches (inc fetches))})))
