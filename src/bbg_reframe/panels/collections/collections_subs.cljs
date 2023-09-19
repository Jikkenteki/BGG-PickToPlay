(ns bbg-reframe.panels.collections.collections-subs
  (:require [bbg-reframe.model.collection :refer [collections-of-the-game
                                                  get-collections]]
            [bbg-reframe.subs :as subs]
            [re-frame.core :as re-frame]))

;; ;; auth-collections
;; (re-frame/reg-sub
;;  ::collections-auth
;;  :<- [::auth-subs/on-auth-value ["collections"]]
;;  (fn [coll [_]]
;;    coll))

;; read from db since fb might be offline
;; auth-collections
(re-frame/reg-sub
 ::collections
 (fn [db]
   (get-collections (:collections db))))

(re-frame/reg-sub
 ::collection
 (fn [db [_ id]]
   (get-in db [:collections (keyword id)])))

(re-frame/reg-sub
 ::collection-games
 (fn [[_ collection-id]]
   [(re-frame/subscribe [::collection collection-id])
    (re-frame/subscribe [::subs/games])])
 (fn [[collection games] [_ _]]
   (let [game-keys (keys (:games collection))]
     (when (sequential? game-keys)
       (map
        (fn [game-id]
          (let [game (get games (name game-id))]
            (println "GAME in collection")
            (if (nil? (:name game))
              {:name "GAME NOT FETCHED YET" :id game-id}
              game)))
        (filter (comp not nil?) game-keys))))))


(re-frame/reg-sub
 ::game-collections
 (fn [[_ game-id]]
   [(re-frame/subscribe [::subs/game game-id])
    (re-frame/subscribe [::collections])])
 (fn [[game collections] [_ _]]
   (collections-of-the-game (:id game) collections)))


;; (collections-of-game (:id game) collections)

(comment
  (def collection {:games {:17133 true, :31260 true}, :name "collection name"})
  (def games {"17133" {:id "17133"
                       :name "17133 name"}
              "31260" {:id "31260"
                       :name "31260 name"}})

  (map
   (fn [game-id] (get games (name game-id)))
   (keys (:games collection)))

  (def collections [{:id :-NT_4XNJNW3gGsUUSPHc,
                     :name "new collection",
                     :games {:22766 true, :31260 true}}
                    {:id :-NT_CIY7FEBG5klVRh3N,
                     :name "fdsafa",
                     :games {:31260 true}}
                    {:id :-NTc6ztoiA_6GQgPNJyA,
                     :name "asa",
                     :games {:6472 true}}])

  (defn collections-the-game-belongs
    [game-id]
    (filter not-empty (map (fn [collection] (if (game-id (:games collection))
                                              collection
                                              nil)) collections)))

  (collections-the-game-belongs :22766)


  (keys (:games collection))

  @(re-frame/subscribe [::collection :-NTYfZCrLuef0fepwFO1])

  @(re-frame/subscribe [::collection-games (name :-NTYfZCrLuef0fepwFO1)])

  ;
  )