(ns bbg-reframe.model.http
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.pprint :refer [pprint]]
            [cljs.core.async :refer [<!]]
            [bbg-reframe.model.db :refer [xml-to-clj]]))

(def xml-str (atom ""))
(defn get-xml  
  [xml-atom game-id]
  (go (reset! xml-atom (:body (<! (http/get (str "http://0.0.0.0:8080/https://boardgamegeek.com/xmlapi/boardgame/" game-id)
                                ;; parameters
                                  {:with-credentials? false
                                   :query-params {}}))))))
(comment
  (def xml-str (atom ""))
  (reset! xml-str "Hello")
  @xml-str
  (xml-to-clj @xml-str true)
  (def xml (get-xml "2651"))
  (pprint @xml-str)

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



  (prn 1)
  )