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
(declare-fun sym209 () String)
(declare-fun sym161 () String)
(declare-fun L0  () String)
(assert (toLower (str.substr sym209  0 1) L0 ))

(assert (= "y" "y" ))
(assert (not (= "" "y" )))
(assert (not (= "" L0 )))
(assert (not (= "" "y" )))
(assert (= "" "" ))
(assert (not (= "" "q" )))
(assert (= "" sym161 ))
(assert (not (= "y" "n" )))
(assert (= L0 "y" ))
(assert (not (= "" sym209 )))
(check-sat)
(get-model)
(exit)
