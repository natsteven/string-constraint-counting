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
(declare-fun sym205 () String)
(declare-fun sym328 () String)
(declare-fun sym605 () String)
(declare-fun sym649 () String)
(declare-fun sym409 () String)
(declare-fun sym729 () String)
(declare-fun sym363 () String)
(declare-fun sym683 () String)
(declare-fun sym289 () String)
(declare-fun sym443 () String)
(declare-fun sym489 () String)
(declare-fun sym567 () String)
(declare-fun sym160 () String)
(declare-fun sym338 () String)
(declare-fun sym659 () String)
(declare-fun sym615 () String)
(declare-fun sym739 () String)
(declare-fun sym373 () String)
(declare-fun sym175 () String)
(declare-fun sym693 () String)
(declare-fun sym254 () String)
(declare-fun sym453 () String)
(declare-fun sym299 () String)
(declare-fun sym135 () String)
(declare-fun sym773 () String)
(declare-fun sym499 () String)
(declare-fun sym533 () String)
(declare-fun sym215 () String)
(declare-fun sym20 () String)
(declare-fun sym625 () String)
(declare-fun sym703 () String)
(declare-fun sym749 () String)
(declare-fun sym509 () String)
(declare-fun sym264 () String)
(declare-fun sym463 () String)
(declare-fun sym783 () String)
(declare-fun sym389 () String)
(declare-fun sym543 () String)
(declare-fun sym423 () String)
(declare-fun sym225 () String)
(declare-fun sym185 () String)
(declare-fun sym581 () String)
(declare-fun sym719 () String)
(declare-fun sym239 () String)
(declare-fun sym759 () String)
(declare-fun sym639 () String)
(declare-fun sym519 () String)
(declare-fun sym793 () String)
(declare-fun sym353 () String)
(declare-fun sym673 () String)
(declare-fun sym113 () String)
(declare-fun sym399 () String)
(declare-fun sym553 () String)
(declare-fun sym433 () String)
(declare-fun sym279 () String)
(declare-fun sym313 () String)
(declare-fun sym479 () String)
(declare-fun sym150 () String)
(declare-fun sym195 () String)
(declare-fun sym591 () String)
(declare-fun L106  () String)
(declare-fun L118  () String)
(declare-fun L7  () String)
(declare-fun L3  () String)
(declare-fun L110  () String)
(declare-fun L102  () String)
(declare-fun L114  () String)
(declare-fun L38  () String)
(declare-fun L18  () String)
(declare-fun L34  () String)
(declare-fun L78  () String)
(declare-fun L46  () String)
(declare-fun L14  () String)
(declare-fun L58  () String)
(declare-fun L26  () String)
(declare-fun L30  () String)
(declare-fun L74  () String)
(declare-fun L8  () String)
(declare-fun L42  () String)
(declare-fun L86  () String)
(declare-fun L10  () String)
(declare-fun L54  () String)
(declare-fun L98  () String)
(declare-fun L22  () String)
(declare-fun L66  () String)
(declare-fun L70  () String)
(declare-fun L4  () String)
(declare-fun L82  () String)
(declare-fun L50  () String)
(declare-fun L94  () String)
(declare-fun L62  () String)
(declare-fun L0  () String)
(declare-fun L90  () String)
(declare-fun L17  () String)
(declare-fun L29  () String)
(declare-fun L13  () String)
(declare-fun L9  () String)
(declare-fun L104  () String)
(declare-fun L116  () String)
(declare-fun L5  () String)
(declare-fun L108  () String)
(declare-fun L1  () String)
(declare-fun L100  () String)
(declare-fun L112  () String)
(declare-fun L16  () String)
(declare-fun L28  () String)
(declare-fun L12  () String)
(declare-fun L56  () String)
(declare-fun L24  () String)
(declare-fun L68  () String)
(declare-fun L36  () String)
(declare-fun L48  () String)
(declare-fun L52  () String)
(declare-fun L96  () String)
(declare-fun L20  () String)
(declare-fun L64  () String)
(declare-fun L32  () String)
(declare-fun L76  () String)
(declare-fun L6  () String)
(declare-fun L44  () String)
(declare-fun L88  () String)
(declare-fun L92  () String)
(declare-fun L60  () String)
(declare-fun L72  () String)
(declare-fun L2  () String)
(declare-fun L40  () String)
(declare-fun L84  () String)
(declare-fun L80  () String)
(assert (toLower sym773  L80 ))
(assert (toLower sym499  L84 ))
(assert (toLower sym533  L40 ))
(assert (toLower sym463  L2 ))
(assert (toLower sym625  L72 ))
(assert (toLower sym693  L60 ))
(assert (toLower sym509  L92 ))
(assert (toLower sym749  L88 ))
(assert (toLower sym739  L44 ))
(assert (toLower sym373  L6 ))
(assert (toLower sym175  L76 ))
(assert (toLower sym185  L32 ))
(assert (toLower sym254  L64 ))
(assert (toLower sym215  L20 ))
(assert (toLower sym703  L96 ))
(assert (toLower sym683  L52 ))
(assert (toLower sym581  L48 ))
(assert (toLower sym567  L36 ))
(assert (toLower sym225  L68 ))
(assert (toLower sym659  L24 ))
(assert (toLower sym423  L56 ))
(assert (toLower sym673  L12 ))
(assert (toLower sym195  L28 ))
(assert (toLower sym299  L16 ))
(assert (toLower sym409  L112 ))
(assert (toLower sym759  L100 ))
(assert (toLower "length"  L1 ))
(assert (toLower sym20  L108 ))
(assert (toLower "database"  L5 ))
(assert (toLower sym649  L116 ))
(assert (toLower sym353  L104 ))
(assert (toLower "description"  L9 ))
(assert (toLower "name"  L13 ))
(assert (toLower "table"  L29 ))
(assert (toLower "field"  L17 ))
(assert (toLower sym443  L90 ))
(assert (toLower sym453  L0 ))
(assert (toLower sym433  L62 ))
(assert (toLower sym389  L94 ))
(assert (toLower sym160  L50 ))
(assert (toLower sym279  L82 ))
(assert (toLower sym113  L4 ))
(assert (toLower sym489  L70 ))
(assert (toLower sym289  L66 ))
(assert (toLower sym719  L22 ))
(assert (toLower sym313  L98 ))
(assert (toLower sym553  L54 ))
(assert (toLower sym363  L10 ))
(assert (toLower sym605  L86 ))
(assert (toLower sym615  L42 ))
(assert (toLower sym783  L8 ))
(assert (toLower sym338  L74 ))
(assert (toLower sym519  L30 ))
(assert (toLower sym135  L26 ))
(assert (toLower sym639  L58 ))
(assert (toLower sym205  L14 ))
(assert (toLower sym399  L46 ))
(assert (toLower sym328  L78 ))
(assert (toLower sym150  L34 ))
(assert (toLower sym793  L18 ))
(assert (toLower sym239  L38 ))
(assert (toLower sym479  L114 ))
(assert (toLower sym264  L102 ))
(assert (toLower sym729  L110 ))
(assert (toLower "option"  L3 ))
(assert (toLower "type"  L7 ))
(assert (toLower sym591  L118 ))
(assert (toLower sym543  L106 ))

(assert (not (= L0 L1 )))
(assert (= L2 L3 ))
(assert (= L4 L5 ))
(assert (= L6 L7 ))
(assert (not (= L8 L9 )))
(assert (not (= L10 L9 )))
(assert (not (= L12 L13 )))
(assert (not (= L14 L13 )))
(assert (= L16 L17 ))
(assert (not (= L18 L17 )))
(assert (not (= L20 L9 )))
(assert (not (= L22 L13 )))
(assert (= L24 L7 ))
(assert (= L26 L13 ))
(assert (not (= L28 L29 )))
(assert (= L30 L3 ))
(assert (not (= L32 L9 )))
(assert (not (= L34 L13 )))
(assert (= L36 L13 ))
(assert (= L38 L13 ))
(assert (not (= L40 L13 )))
(assert (not (= L42 L9 )))
(assert (not (= L44 L7 )))
(assert (not (= L46 L9 )))
(assert (not (= L48 L13 )))
(assert (= L50 L9 ))
(assert (not (= L52 L9 )))
(assert (= L54 L17 ))
(assert (not (= L56 L13 )))
(assert (not (= L58 L13 )))
(assert (not (= L60 L7 )))
(assert (not (= L62 L9 )))
(assert (not (= L64 L13 )))
(assert (not (= L66 L9 )))
(assert (= L68 L29 ))
(assert (not (= L70 L9 )))
(assert (= L72 L7 ))
(assert (= L74 L9 ))
(assert (not (= L76 L13 )))
(assert (not (= L78 L13 )))
(assert (not (= L80 L13 )))
(assert (not (= L82 L13 )))
(assert (not (= L84 L7 )))
(assert (not (= L86 L13 )))
(assert (not (= L88 L1 )))
(assert (not (= L90 L7 )))
(assert (not (= L92 L1 )))
(assert (not (= L94 L13 )))
(assert (= L96 L1 ))
(assert (= L98 L13 ))
(assert (= L100 L3 ))
(assert (= L102 L9 ))
(assert (not (= L104 L13 )))
(assert (not (= L106 L9 )))
(assert (= L108 "html" ))
(assert (not (= L108 "sql" )))
(assert (not (= L110 L9 )))
(assert (= L112 L7 ))
(assert (not (= L114 L13 )))
(assert (not (= L116 L9 )))
(assert (= L118 L9 ))
(check-sat)
(get-model)
(exit)
