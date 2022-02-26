(ns bbg-reframe.model.localstorage)

(defn set-item!
  "Set `key' in browser's localStorage to `val`."
  [key val]
  (.setItem (.-localStorage js/window) key val))

(defn get-item
  "Returns value of `key' from browser's localStorage."
  [key]
  (.getItem (.-localStorage js/window) key))


(defn item-exists?
  "Checks if `key' exists in browser's localStorage."
  [key]
  (.hasOwnProperty (.-localStorage js/window) key))

(defn remove-item!
  "Remove the browser's localStorage value for the given `key`"
  [key]
  (.removeItem (.-localStorage js/window) key))

(comment
  (remove-item! "ls-key"))
;; 
;; slurp and spit implementations using local storage
;; 
(defn spit
  "Uses local storage for saving the data into the file"
  [fname data]
  (set-item! fname data))

;; (defn read-ls
;;   "Uses local storage for retrieving the file"
;;   [fname]
;;   (get-item fname))