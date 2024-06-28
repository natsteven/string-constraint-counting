(set-logic QF_S)
(set-option :parse-transducers true)
(set-option :produce-models true)
(define-fun-rec toLower ((x String) (y String)) Bool
   (or (and (= x "") (= y ""))
       (and (not (= x "")) (not (= y ""))
           (= (char.code (str.head y))
               (ite (and (<= 65 (char.code (str.head x)))
                       (<= (char.code (str.head x)) 90))
                   (+ (char.code (str.head x)) 32)
                   (char.code (str.head x))))
           (toLower (str.tail x) (str.tail y))))
)
(declare-fun sym743 () String)
(declare-fun sym205 () String)
(declare-fun sym547 () String)
(declare-fun sym161 () String)
(declare-fun L3  () String)
(declare-fun L1  () String)
(declare-fun L0  () String)
(assert (toLower (str.substr sym205  0 1) L0 ))
(assert (toLower (str.substr sym547  0 1) L1 ))
(assert (toLower (str.substr sym743  0 1) L3 ))

(assert (= "y" "y" ))
(assert (= L0 "y" ))
(assert (not (= "" sym205 )))
(assert (not (= "" "y" )))
(assert (= L1 "r" ))
(assert (not (= L1 "g" )))
(assert (not (= "" "q" )))
(assert (not (= "" sym743 )))
(assert (= "" "" ))
(assert (= L3 "r" ))
(assert (not (= "" sym547 )))
(assert (not (= L1 "p" )))
(assert (not (= "y" "n" )))
(assert (not (= "" L0 )))
(assert (not (= L3 "q" )))
(assert (not (= "" "y" )))
(assert (not (= L1 "k" )))
(assert (not (= L1 "q" )))
(assert (not (= L3 "p" )))
(assert (not (= L3 "g" )))
(assert (not (= L3 "k" )))
(assert (not (= "" sym161 )))
(check-sat)
(get-model)
(exit)
