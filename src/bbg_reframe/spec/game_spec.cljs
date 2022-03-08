(ns bbg-reframe.spec.game-spec
  (:require [clojure.spec.alpha :as s]))

(def non-negative-number? (s/and number? #(>= % 0)))
(def non-negative-number-or-nil? (s/or :non-negative-number non-negative-number? :nil nil?))
(def number-or-nil? (s/or :number number? :nil nil?))
(comment
  (true? (s/valid? non-negative-number-or-nil? nil))
  (true? (s/valid? non-negative-number-or-nil? 1))
  (false? (s/valid? non-negative-number-or-nil? ""))

;
  )
(def percentage? (s/and double? #(>= % 0.0) #(<= % 1.0)))
(def non-empty-string? (s/and string? #(seq %)))

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
(s/def ::name non-empty-string?)
(s/def ::rating non-negative-number?)
(s/def ::my-rating non-negative-number-or-nil?)
(s/def ::playingtime non-negative-number-or-nil?)
(s/def ::minplayers non-negative-number?)
(s/def ::maxplayers non-negative-number?)
(s/def ::yearpublished number-or-nil?)
(s/def ::thumbnail non-empty-string?)

(s/def ::friendlyname non-empty-string?)
(s/def ::value non-negative-number-or-nil?) ;; "Not Ranked" -> nil

(s/def ::rank (s/keys :req-un [::friendlyname ::value]))
(s/def ::ranks (s/coll-of ::rank))

(s/def ::game
  (s/keys :req-un [::id
                   ::name
                   ::rating
                   ::my-rating
                   ::playingtime
                   ::type
                   ::minplayers
                   ::maxplayers
                   ::thumbnail
                   ::ranks
                   ::yearpublished]
          :opt-un [::votes]))
