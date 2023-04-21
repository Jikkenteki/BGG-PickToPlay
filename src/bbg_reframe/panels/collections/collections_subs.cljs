(ns bbg-reframe.panels.collections.collections-subs
  (:require [bbg-reframe.forms.forms :refer [db-get-ref]]
            [bbg-reframe.model.collection :refer [get-collections]]
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
   (map
    (fn [game-id] (get games (name game-id)))
    (keys (:games collection)))))

(comment
  (def collection {:games {:17133 true, :31260 true, :43111 true, :251247 true}, :name "agafds"})
  (keys (:games collection))

  @(re-frame/subscribe [::collection :-NTYfZCrLuef0fepwFO1])

  @(re-frame/subscribe [::collection-games (name :-NTYfZCrLuef0fepwFO1)])

  ;
  )