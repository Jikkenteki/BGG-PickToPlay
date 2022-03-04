(ns bbg-reframe.db
  (:require [clojure.spec.alpha :as s]
            [re-frame.db]))

(def non-negative-number? (s/and number? #(>= % 0)))
(def string-or-nil? (s/or :string string? :nil nil?))

(def percentage? (s/and double? #(>= % 0.0) #(<= % 1.0)))
(def non-empty-string? (s/and string? #(> (count %) 0)))

(s/def ::best-votes non-negative-number?)
(s/def ::best-perc percentage?)
(s/def ::recommended-votes non-negative-number?)
(s/def ::recommended-perc percentage?)
(s/def ::not-recommended-votes non-negative-number?)
(s/def ::not-recommended-perc percentage?)
(s/def ::votes-per-players (s/keys :req-un
                                   [::players
                                    ::best-votes ::best-perc
                                    ::recommended-votes ::recommended-perc
                                    ::not-recommended-votes ::not-recommended-perc]))
(s/def ::votes (s/coll-of ::votes-per-players))

(s/def ::id non-empty-string?)
(s/def ::game
  (s/keys :req-un [::id ::name ::rating ::my-rating ::playingtime
                   ::type ::minplayers ::maxplayers]
          :opt-un [::votes]))

(s/def ::game-unchecked (s/keys))

(s/def ::user string-or-nil?)
(s/def ::cors-running boolean?)
(s/def ::fetches non-negative-number?)
(s/def ::form (s/keys :req-un
                      [::sort-id ::take ::higher-than ::players ::threshold ::time-limit ::show]))
(s/def ::games (s/map-of ::id ::game-unchecked))
(s/def ::result (s/coll-of ::game-unchecked))
(s/def ::error string-or-nil?)
(s/def ::ui (s/keys :req-un [::open-tab]))
(s/def ::queue (s/coll-of ::id))
(s/def ::fetching (s/coll-of ::id))

(s/def ::db
  (s/keys :req-un
          [::result
           ::form
           ::games
           ::queue
           ::fetching
           ::fetches
           ::bg-loading
           ::error
           ::cors-running
           ::user
           ::ui]))

(def default-db
  {:result []
   :fields ["name"]
   :form {:sort-id "playability"
          :take "10"
          :higher-than "7.0"
          :players "4"
          :threshold "0.8"
          :time-limit "180"
          :show "boardgame" ;; "any" "boardgame" ;; change to keywords :all :boardgame
          }
   :games {}
   :queue #{}
   :fetching #{}
   :fetches 0
   :bg-loading false
   :error nil
   :cors-running true
   :user nil
   :ui {:open-tab ""}})



(comment
  (s/def ::user (s/or :string string? :nil nil?))
  (s/def ::cors-running boolean?)
  (s/def ::fetches (s/and number? #(>= 0 %)))
  (s/def ::form (s/keys :req-un [::sort-id ::take ::higher-than ::players ::threshold ::time-limit ::show]))
  (s/def ::db-spec
    (s/keys :req-un
            [::result
             ::form
             ::games
             ::queue
             ::fetching
             ::fetches
             ::bg-loading
             ::error
             ::cors-running
             ::user
             ::ui]))

  (s/valid? ::db @re-frame.db/app-db)
  (println (s/explain-str ::db @re-frame.db/app-db))
  (s/valid? ::db-spec default-db)
  (s/explain-str ::db-spec default-db)
  default-db
  ;
  )