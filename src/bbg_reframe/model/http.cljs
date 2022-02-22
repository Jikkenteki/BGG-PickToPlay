(ns bbg-reframe.model.http
  (:require [cljs-http.client :as http]))

(comment
  (http/get "https://boardgamegeek.com/xmlapi2/plays?username=ddmits&id=167791"
            {:channel a-channel})
  ;
  )