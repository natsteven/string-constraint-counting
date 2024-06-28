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
(declare-fun r4 () String)
(declare-fun L0  () String)
(assert (toLower (str.substr r4  0 1) L0 ))

(assert (not (= L0 "p" )))
(assert (= L0 "l" ))
(assert (= "y" "y" ))
(assert (not (= "" r4 )))
(assert (not (= "" r4 )))
(assert (not (= L0 "k" )))
(assert (not (= "" r4 )))
(assert (not (= L0 "r" )))
(assert (not (= L0 "q" )))
(assert (= L0 "g" ))
(assert (not (= L0 "q" )))
(assert (not (= L0 "q" )))
(assert (= L0 "g" ))
(assert (= L0 "p" ))
(assert (not (= "" L0 )))
(assert (not (= L0 "k" )))
(assert (not (= "y" "n" )))
(assert (not (= L0 "g" )))
(assert (not (= L0 "q" )))
(assert (not (= L0 "r" )))
(assert (not (= "" r4 )))
(assert (not (= L0 "q" )))
(assert (= L0 "h" ))
(assert (not (= "" r4 )))
(assert (not (= L0 "k" )))
(assert (not (= L0 "r" )))
(assert (not (= "" "y" )))
(assert (not (= "" r4 )))
(assert (not (= L0 "p" )))
(assert (not (= L0 "q" )))
(assert (not (= "" "q" )))
(assert (= "" "" ))
(assert (not (= "" r4 )))
(assert (not (= L0 "p" )))
(assert (= L0 "h" ))
(assert (not (= L0 "q" )))
(assert (not (= L0 "l" )))
(assert (not (= L0 "g" )))
(assert (not (= L0 "k" )))
(assert (= L0 "g" ))
(assert (not (= L0 "p" )))
(assert (not (= L0 "l" )))
(assert (not (= "" r4 )))
(assert (not (= "" r4 )))
(assert (not (= "" "y" )))
(assert (not (= L0 "q" )))
(assert (= L0 "h" ))
(assert (not (= "" L0 )))
(assert (= L0 "g" ))
(assert (not (= L0 "q" )))
(assert (not (= L0 "q" )))
(assert (not (= L0 "g" )))
(assert (not (= L0 "k" )))
(assert (not (= L0 "k" )))
(assert (not (= L0 "q" )))
(assert (not (= L0 "l" )))
(assert (= L0 "p" ))
(assert (not (= "" r4 )))
(assert (= L0 "g" ))
(assert (not (= "" r4 )))
(assert (not (= L0 "k" )))
(assert (not (= "" r4 )))
(assert (not (= "" "n" )))
(assert (not (= "" r4 )))
(assert (= L0 "p" ))
(assert (not (= L0 "q" )))
(assert (= L0 "p" ))
(assert (not (= L0 "r" )))
(assert (not (= L0 "k" )))
(assert (not (= L0 "y" )))
(assert (not (= L0 "q" )))
(assert (not (= L0 "k" )))
(assert (not (= L0 "q" )))
(assert (not (= L0 "p" )))
(assert (= L0 "p" ))
(assert (not (= L0 "p" )))
(assert (= L0 "p" ))
(assert (= L0 "y" ))
(assert (not (= L0 "p" )))
(assert (not (= L0 "p" )))
(assert (not (= L0 "q" )))
(assert (not (= "" r4 )))
(assert (not (= L0 "k" )))
(assert (not (= L0 "r" )))
(assert (not (= L0 "l" )))
(assert (not (= L0 "g" )))
(assert (= L0 "g" ))
(assert (not (= L0 "q" )))
(assert (not (= L0 "p" )))
(assert (not (= "" r4 )))
(assert (not (= "" r4 )))
(assert (not (= L0 "k" )))
(assert (= L0 "h" ))
(assert (not (= "n" "y" )))
(assert (not (= "" r4 )))
(assert (not (= L0 "q" )))
(assert (not (= "" r4 )))
(assert (not (= "" r4 )))
(assert (not (= L0 "p" )))
(assert (not (= L0 "q" )))
(assert (not (= "" r4 )))
(assert (not (= "" r4 )))
(assert (not (= L0 "g" )))
(assert (= L0 "p" ))
(assert (not (= L0 "p" )))
(check-sat)
(get-model)
(exit)
