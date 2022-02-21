(ns bbg-reframe.model.http
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(defn get-xml  
  [game-id]
  (go (:body (<! (http/get (str "http://0.0.0.0:8080/https://boardgamegeek.com/xmlapi/boardgame/" game-id)
                                ;; parameters
                                  {:with-credentials? false
                                   :query-params {}})))))
(comment
  (go ((prn "Calling")
       (prn  (:body (<! (http/get "http://0.0.0.0:8080/https://boardgamegeek.com/xmlapi/boardgame/2651"
                                ;; parameters
                                  {:with-credentials? false
                                   :query-params {}}))))))

  (go ((prn "Calling")
       (prn  (:body (<! (http/get "https://boardgamegeek.com"))))))

  (go ((prn "Calling")
       (prn  (<! (http/get "http://localhost:8280/")))))

  (prn 1)

  (-> (js/fetch "http://0.0.0.0:8080/https://boardgamegeek.com/xmlapi/boardgame/2651"
      (.then #(.html %))
      (.then prn)))

  

  (prn 1))