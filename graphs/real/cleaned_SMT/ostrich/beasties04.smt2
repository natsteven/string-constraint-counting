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
(declare-fun sym6582 () String)
(declare-fun sym807 () String)
(declare-fun sym7456 () String)
(declare-fun sym7830 () String)
(declare-fun sym6369 () String)
(declare-fun sym7018 () String)
(declare-fun sym205 () String)
(declare-fun sym547 () String)
(declare-fun sym6802 () String)
(declare-fun sym1446 () String)
(declare-fun sym1865 () String)
(declare-fun sym2514 () String)
(declare-fun sym5744 () String)
(declare-fun sym4236 () String)
(declare-fun sym2078 () String)
(declare-fun sym5103 () String)
(declare-fun sym3187 () String)
(declare-fun sym1020 () String)
(declare-fun sym6170 () String)
(declare-fun sym161 () String)
(declare-fun sym4691 () String)
(declare-fun sym8013 () String)
(declare-fun sym2291 () String)
(declare-fun sym7225 () String)
(declare-fun sym7647 () String)
(declare-fun sym1659 () String)
(declare-fun sym4904 () String)
(declare-fun sym2727 () String)
(declare-fun sym5957 () String)
(declare-fun sym2940 () String)
(declare-fun sym3810 () String)
(declare-fun sym5316 () String)
(declare-fun sym3611 () String)
(declare-fun sym1233 () String)
(declare-fun sym4465 () String)
(declare-fun sym3396 () String)
(declare-fun sym5531 () String)
(declare-fun sym4023 () String)
(declare-fun L17  () String)
(declare-fun L29  () String)
(declare-fun L19  () String)
(declare-fun L23  () String)
(declare-fun L45  () String)
(declare-fun L13  () String)
(declare-fun L9  () String)
(declare-fun L25  () String)
(declare-fun L47  () String)
(declare-fun L15  () String)
(declare-fun L59  () String)
(declare-fun L5  () String)
(declare-fun L21  () String)
(declare-fun L11  () String)
(declare-fun L3  () String)
(declare-fun L1  () String)
(declare-fun L102  () String)
(declare-fun L38  () String)
(declare-fun L28  () String)
(declare-fun L18  () String)
(declare-fun L12  () String)
(declare-fun L34  () String)
(declare-fun L24  () String)
(declare-fun L14  () String)
(declare-fun L36  () String)
(declare-fun L48  () String)
(declare-fun L52  () String)
(declare-fun L74  () String)
(declare-fun L42  () String)
(declare-fun L86  () String)
(declare-fun L32  () String)
(declare-fun L6  () String)
(declare-fun L22  () String)
(declare-fun L4  () String)
(declare-fun L60  () String)
(declare-fun L2  () String)
(declare-fun L0  () String)
(assert (toLower (str.substr sym1233  0 1) L0 ))
(assert (toLower (str.substr sym5744  0 1) L2 ))
(assert (toLower (str.substr sym4691  0 1) L60 ))
(assert (toLower (str.substr sym7456  0 1) L4 ))
(assert (toLower (str.substr sym1865  0 1) L22 ))
(assert (toLower (str.substr sym7830  0 1) L6 ))
(assert (toLower (str.substr sym5531  0 1) L32 ))
(assert (toLower (str.substr sym2727  0 1) L86 ))
(assert (toLower (str.substr sym5316  0 1) L42 ))
(assert (toLower (str.substr sym547  0 1) L74 ))
(assert (toLower (str.substr sym7647  0 1) L52 ))
(assert (toLower (str.substr sym7018  0 1) L48 ))
(assert (toLower (str.substr sym4904  0 1) L36 ))
(assert (toLower (str.substr sym205  0 1) L14 ))
(assert (toLower (str.substr sym3611  0 1) L24 ))
(assert (toLower (str.substr sym2940  0 1) L34 ))
(assert (toLower (str.substr sym2291  0 1) L12 ))
(assert (toLower (str.substr sym4236  0 1) L18 ))
(assert (toLower (str.substr sym1020  0 1) L28 ))
(assert (toLower (str.substr sym4023  0 1) L38 ))
(assert (toLower (str.substr sym6170  0 1) L102 ))
(assert (toLower (str.substr sym6369  0 1) L1 ))
(assert (toLower (str.substr sym6802  0 1) L3 ))
(assert (toLower (str.substr sym6582  0 1) L11 ))
(assert (toLower (str.substr sym3187  0 1) L21 ))
(assert (toLower (str.substr sym1659  0 1) L5 ))
(assert (toLower (str.substr sym2514  0 1) L59 ))
(assert (toLower (str.substr sym4465  0 1) L15 ))
(assert (toLower (str.substr sym3810  0 1) L47 ))
(assert (toLower (str.substr sym5103  0 1) L25 ))
(assert (toLower (str.substr sym1446  0 1) L9 ))
(assert (toLower (str.substr sym3396  0 1) L13 ))
(assert (toLower (str.substr sym807  0 1) L45 ))
(assert (toLower (str.substr sym5957  0 1) L23 ))
(assert (toLower (str.substr sym8013  0 1) L19 ))
(assert (toLower (str.substr sym7225  0 1) L29 ))
(assert (toLower (str.substr sym2078  0 1) L17 ))

(assert (not (= "" sym3187 )))
(assert (not (= L0 "q" )))
(assert (not (= L1 "p" )))
(assert (not (= L2 "p" )))
(assert (not (= L3 "g" )))
(assert (not (= "" sym5316 )))
(assert (= L4 "k" ))
(assert (not (= L5 "q" )))
(assert (not (= "" sym7225 )))
(assert (not (= "" sym4236 )))
(assert (= L6 "k" ))
(assert (not (= L6 "q" )))
(assert (not (= L3 "q" )))
(assert (not (= L9 "q" )))
(assert (= L3 "h" ))
(assert (= L11 "k" ))
(assert (not (= L12 "q" )))
(assert (not (= "" "y" )))
(assert (not (= "" sym4691 )))
(assert (not (= L13 "q" )))
(assert (= L14 "y" ))
(assert (= L15 "r" ))
(assert (not (= "" sym6369 )))
(assert (not (= L11 "q" )))
(assert (= L17 "k" ))
(assert (not (= "" "q" )))
(assert (not (= "" sym1659 )))
(assert (= L18 "k" ))
(assert (not (= L19 "q" )))
(assert (not (= L9 "p" )))
(assert (not (= L21 "k" )))
(assert (not (= "" sym5531 )))
(assert (= "y" "y" ))
(assert (= L22 "k" ))
(assert (not (= L23 "q" )))
(assert (= L24 "k" ))
(assert (= L25 "k" ))
(assert (= L0 "k" ))
(assert (not (= "" sym4023 )))
(assert (not (= L17 "p" )))
(assert (not (= L28 "p" )))
(assert (not (= "" sym807 )))
(assert (not (= "" sym2078 )))
(assert (not (= "" sym4465 )))
(assert (not (= L29 "q" )))
(assert (not (= L13 "k" )))
(assert (not (= L15 "q" )))
(assert (not (= L32 "q" )))
(assert (not (= L4 "p" )))
(assert (not (= L34 "p" )))
(assert (= L32 "k" ))
(assert (= L36 "k" ))
(assert (not (= L3 "k" )))
(assert (not (= L38 "p" )))
(assert (not (= L5 "p" )))
(assert (not (= "" sym1865 )))
(assert (not (= L32 "p" )))
(assert (not (= L21 "p" )))
(assert (not (= "" sym5103 )))
(assert (not (= "" sym6170 )))
(assert (= L42 "h" ))
(assert (not (= L15 "k" )))
(assert (not (= L12 "p" )))
(assert (= L45 "k" ))
(assert (not (= "" sym3810 )))
(assert (not (= "" sym6582 )))
(assert (not (= L24 "p" )))
(assert (not (= "" sym4904 )))
(assert (not (= L47 "p" )))
(assert (not (= "" sym7647 )))
(assert (not (= "" sym2940 )))
(assert (= L48 "k" ))
(assert (not (= L22 "p" )))
(assert (not (= L42 "l" )))
(assert (not (= L15 "p" )))
(assert (not (= L52 "p" )))
(assert (not (= L52 "q" )))
(assert (not (= L3 "r" )))
(assert (not (= L42 "r" )))
(assert (not (= L22 "q" )))
(assert (not (= L48 "p" )))
(assert (= L12 "h" ))
(assert (not (= "" sym7456 )))
(assert (not (= L59 "p" )))
(assert (not (= "" sym6802 )))
(assert (not (= "" sym1446 )))
(assert (not (= L60 "p" )))
(assert (not (= "y" "n" )))
(assert (not (= L25 "q" )))
(assert (not (= L12 "r" )))
(assert (= L60 "k" ))
(assert (not (= "" sym1233 )))
(assert (not (= L28 "q" )))
(assert (not (= L42 "k" )))
(assert (not (= L12 "k" )))
(assert (= L1 "k" ))
(assert (= L52 "k" ))
(assert (not (= L60 "q" )))
(assert (not (= L1 "q" )))
(assert (= L19 "k" ))
(assert (not (= L18 "q" )))
(assert (not (= L13 "r" )))
(assert (not (= L74 "p" )))
(assert (not (= L25 "p" )))
(assert (not (= L36 "q" )))
(assert (not (= L4 "q" )))
(assert (not (= "" sym7830 )))
(assert (not (= L29 "p" )))
(assert (not (= L13 "g" )))
(assert (not (= L3 "l" )))
(assert (not (= L42 "q" )))
(assert (not (= L13 "p" )))
(assert (not (= L23 "p" )))
(assert (not (= L0 "p" )))
(assert (not (= L17 "q" )))
(assert (not (= "" sym3611 )))
(assert (not (= L86 "p" )))
(assert (not (= "" sym3396 )))
(assert (= L2 "k" ))
(assert (not (= L59 "q" )))
(assert (not (= "" sym8013 )))
(assert (= L34 "k" ))
(assert (not (= L13 "l" )))
(assert (not (= "" "y" )))
(assert (not (= "" sym2291 )))
(assert (not (= L74 "q" )))
(assert (not (= L48 "q" )))
(assert (not (= L18 "p" )))
(assert (not (= "" sym5957 )))
(assert (not (= L21 "q" )))
(assert (not (= L12 "l" )))
(assert (not (= L42 "p" )))
(assert (not (= L3 "p" )))
(assert (not (= "" sym1020 )))
(assert (not (= L11 "p" )))
(assert (not (= L47 "q" )))
(assert (= L13 "h" ))
(assert (= L86 "k" ))
(assert (not (= "" sym7018 )))
(assert (not (= L102 "q" )))
(assert (not (= L45 "q" )))
(assert (= L102 "k" ))
(assert (not (= "" L14 )))
(assert (not (= L42 "g" )))
(assert (= "" "" ))
(assert (= L59 "k" ))
(assert (not (= L19 "p" )))
(assert (not (= "" sym547 )))
(assert (= L38 "k" ))
(assert (not (= "" sym161 )))
(assert (not (= L21 "g" )))
(assert (= L9 "k" ))
(assert (not (= "" sym2514 )))
(assert (not (= L2 "q" )))
(assert (not (= L45 "p" )))
(assert (not (= L15 "g" )))
(assert (not (= "" sym2727 )))
(assert (= L74 "k" ))
(assert (= L5 "k" ))
(assert (not (= "" sym205 )))
(assert (not (= L36 "p" )))
(assert (= L21 "r" ))
(assert (= L47 "k" ))
(assert (not (= L34 "q" )))
(assert (not (= L12 "g" )))
(assert (not (= L6 "p" )))
(assert (not (= L86 "q" )))
(assert (not (= L38 "q" )))
(assert (not (= L24 "q" )))
(assert (= L28 "k" ))
(assert (= L29 "k" ))
(assert (= L23 "k" ))
(assert (not (= "" sym5744 )))
(assert (not (= L102 "p" )))
(check-sat)
(get-model)
(exit)
