(ns bbg-reframe.forms.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::set-value!
 (fn-traced
  [db [_ path value]]
  (assoc-in db path value)))

(re-frame/reg-event-db
 ::update-value!
 (fn-traced
  [db [_ path upd-fn]]
  (update-in db path upd-fn)))