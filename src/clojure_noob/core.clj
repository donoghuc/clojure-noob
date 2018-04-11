(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
(defn train
  []
  println "choo choo!")

;; Simple Math
(= (- 10 (* 2 3)) 4)

;; Intro to Functions
(= 8 ((fn add-five [x] (+ x 5)) 3))
(= 8 ((fn [x] (+ x 5)) 3))
(= 8 (#(+ % 5) 3))
(= 8 ((partial + 5) 3))

;; Double Down
(= (#(* % 2) 2) 4)

;; Hello World
(= (#(str "Hello, " % "!") "Dave") "Hello, Dave!")

;; Last Element: Restriction (last)
;;      solution
(fn [x] (if (> (count x) 0)
          (nth x (- (count x) 1))
          nil))
;;       test
(= ((fn [x] (if (> (count x) 0)
          (nth x (- (count x) 1))
          nil)) [1 2 3 4 5]) 5)

;; Regular Expressions
(= "ABC" (apply str (re-seq #"[A-Z]+" "bA1B3Ce ")))

;; Maximum Value: Restriction (max, max-key)
;;    solution
(fn [& acc] (reduce #(if (> %1 %2) %1 %2) acc))
#(reduce (fn [a b] (if (> a b) a b)) %&)
;;    test
(= ((fn [& acc] (reduce #(if (> %1 %2) %1 %2) acc)) 1 8 3 4) 8)

;; Interleaving two Seqs: Restriction (interleave)
;; recursive solution for fun (not allowed)
(defn recur-soln
  ([col-1 col-2] (recur-soln col-1 col-2 []))
  ([col-1 col-2 acc]
   (if (or (empty? col-1) (empty? col-2))
     acc
     (recur (rest col-1) (rest col-2) (conj acc (first col-1) (first col-2))))))
;; soln found
#(mapcat vector %1 %2)

;; simple recursion example
(defn recur-sum
  ([vals] (recur-sum vals 0))
  ([vals acc]
   (if (empty? vals)
     acc
     (recur-sum (rest vals) (+ (first vals) acc)))))
