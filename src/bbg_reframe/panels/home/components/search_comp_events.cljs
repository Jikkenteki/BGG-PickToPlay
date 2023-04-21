(ns bbg-reframe.panels.home.components.search-comp-events 
  (:require [bbg-reframe.model.sort-filter :refer [has-name? name-alpha?]]
            [day8.re-frame.tracing :refer [fn-traced]]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
 ::search-game-name-with-substring
 (fn-traced [{:keys [db]} [_ val]]
            (let [filtered
                  (if (> (count val) 1)
                    (->> (vals (:games db))
                         (filter (has-name? val))
                         (sort name-alpha?))
                    [])]
              {:db (-> db 
                       (assoc-in [:search :substring] val)
                       (assoc-in [:search :search-results] filtered))})))

(re-frame/reg-event-db
 ::reset-search
 (fn-traced [db _]
            (assoc db :search nil)))