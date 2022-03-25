(ns bbg-reframe.forms.bind
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.forms.forms :refer [db-set-value!]]
            [clojure.string :as string]))


(def bind-sub-register (atom #{}))

(defn bind-form-to-sub!
  "Creates a new re-frame subscription with a unique key
   that subscribes to `from-sub` with a handler that
   dispatches a db write event of the value to the `db-path` list of keywords.
   It then subscribes to `new-subscription`.
   The key of the subscription is string combining the from-sub keyword and the members of the db-path list."
  [from-sub db-path]
  ;; create a subscription with a dispatch side-effect
  (let [new-subscription (keyword
                          (str (namespace (first from-sub)) \.
                               (name (first from-sub)) \.
                               (apply str (rest from-sub)) "->" (string/join "." (map name db-path))))]
    (if (@bind-sub-register new-subscription) ;; is member?
      db-path
      (let [_ (swap! bind-sub-register #(conj % new-subscription)) ;; add to register
            _ (print "NEW SUB" new-subscription)]
        (re-frame/reg-sub
         new-subscription
         (fn []
           (re-frame/subscribe from-sub))
         (fn [value]
   ;; changing of the form-path should happer here so that it happens only once
   ;; and not when component rerenders due to drop-down changing the dom
   ;; and causing rerender which resets the value to the one read from 
   ;; the subscription.
           (db-set-value! db-path value)
           value))
  ;; subscribe to the subscription and return its ref
        @(re-frame/subscribe [new-subscription])
        db-path))))

(defn bind-form-to-value!
  [value db-path]
  (println "SUB-VAL" value db-path)
  (db-set-value! db-path value)
  db-path)

(comment
  (#{1 2 3} 4)
  (conj #{1 2} 3)
  (swap! bind-sub-register #(conj % 3))
  bind-form-to-value!
  ;
  )