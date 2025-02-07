# parsesso

Parser combinators for Clojure(Script).

[![cljdoc badge](https://cljdoc.org/badge/com.github.strojure/parsesso)](https://cljdoc.org/d/com.github.strojure/parsesso)
[![Clojars Project](https://img.shields.io/clojars/v/com.github.strojure/parsesso.svg)](https://clojars.org/com.github.strojure/parsesso)

## Motivation

* Idiomatic and convenient API for parser combinators in Clojure and
  ClojureScript.

## Inspiration

* [haskell/parsec](https://github.com/haskell/parsec)
* [blancas/kern](https://github.com/blancas/kern)
* [youngnh/parsatron](https://github.com/youngnh/parsatron)
* [rm-hull/jasentaa](https://github.com/rm-hull/jasentaa)

## Cheat sheet

| Parsesso                              | Parsec<sup>[1],[2],[3]</sup>    | Kern<sup>[4]</sup>      | Parsatron<sup>[5]</sup> |
|---------------------------------------|---------------------------------|-------------------------|-------------------------|
| <code>[p/result]</code>               | `return`                        | `return`                | `always`                |
| <code>[p/fail]</code>                 | `fail`                          | `fail`                  | `never`                 |
| <code>[p/fail-unexpected]</code>      | `unexpected`                    | `unexpected`            |                         |
| <code>[p/expecting]</code>            | `<?>`, `label`                  | `<?>`, `expect`         |                         |
| <code>[p/bind]</code>                 | `>>=`                           | `>>=`                   | `bind`                  |
| <code>[p/for]</code>                  | `do`                            | `bind`                  | `let->>`                |
| <code>[p/after]</code>                | `>>`                            | `>>`                    | `>>`, `nxt`             |
| <code>[p/value]</code>                | `fmap`                          | `<$>`                   |                         |
| <code>[p/maybe]</code>                | `try`                           | `<:>`                   | `attempt`               |
| <code>[p/look-ahead]</code>           | `lookAhead`                     | `look-ahead`            | `lookahead`             |
| <code>[p/not-followed-by]</code>      | `notFollowedBy`                 | `not-followed-by`       |                         |
| <code>[p/*many]</code>                | `many`                          | `many`                  | `many`                  |
| <code>[p/+many]</code>                | `many1`                         | `many1`                 | `many1`                 |
| <code>[p/*skip]</code>                | `skipMany`                      | `skip-many`             |                         |
| <code>[p/+skip]</code>                | `skipMany1`                     | `skip-many1`            |                         |
| <code>[p/token]</code>                | `token`, `satisfy`              | `satisfy`               | `token`                 |
| <code>[p/token-not]</code>            |                                 |                         |                         |
| <code>[p/word]</code>                 | `tokens`, `string`              | `token*`                | `string`                |
| <code>[p/any-token]</code>            | `anyToken`,`anyChar`            | `any-char`              | `any-char`              |
| <code>[p/eof]</code>                  | `eof`                           | `eof`                   | `eof`                   |
| <code>[p/group]</code>                | `<*>`                           | `<*>`                   |                         |
| <code>[p/alt]</code>                  | <code><&#124;></code>, `choice` | <code><&#124;></code>   | `choice`                |
| <code>[p/option]</code>               | `option`, `optional`            | `option`, `optional`    |                         |
| <code>[p/between]</code>              | `between`                       | `between`               | `between`               |
| <code>[p/times]</code>                | `count`                         | `times`                 | `times`                 |
| <code>[p/*many-till]</code>           | `manyTill`                      | `many-till`             |                         |
| <code>[p/*sep-by]</code>              | `sepBy`                         | `sep-by`                |                         |
| <code>[p/+sep-by]</code>              | `sepBy1`                        | `sep-by1`               |                         |
| <code>[p/*sep-end-by]</code>          | `endBy`                         | `end-by`                |                         |
| <code>[p/+sep-end-by]</code>          | `endBy1`                        | `end-by1`               |                         |
| <code>[p/*sep-opt-by]</code>          | `sepEndBy`                      | `sep-end-by`            |                         |
| <code>[p/+sep-opt-by]</code>          | `sepEndBy1`                     | `sep-end-by1`           |                         |
| <code>[p/get-state]</code>            | `getParserState`...             | input, pos, user state  |                         |
| <code>[p/set-state]</code>            | `setParserState`...             | input, pos, user state  |                         |
| <code>[p/update-state]</code>         | `updateParserState`...          | user state              |                         |
| <code>[p/trace]</code>                | `parserTrace`, `parserTraced`   |                         |                         |
| <code>[expr/*chain-left]</code>       | `chainl`                        | `chainl`                |                         |
| <code>[expr/+chain-left]</code>       | `chainl1`                       | `chainl1`               |                         |
| <code>[expr/*chain-right]</code>      | `chainr`                        | `chainr`                |                         |
| <code>[expr/+chain-right]</code>      | `chainr1`                       | `chainr1`               |                         |
| <code>[char/is]</code>                | `char`, `oneOf`                 | `sym*`, `one-of*`       | `char`                  |
| <code>[char/is-not]</code>            | `noneOf`                        | `none-of*`              |                         |
| <code>[char/regex]</code>             |                                 |                         |                         |
| <code>[char/upper?]</code>            | `upper`                         | `upper` (unicode)       |                         |
| <code>[char/lower?]</code>            | `lower`                         | `lower` (unicode)       |                         |
| <code>[char/letter?]</code>           | `letter`                        | `letter` (unicode)      | `letter` (unicode)      |
| <code>[char/number?]</code>           | `digit`                         | `digit` (unicode)       | `digit` (unicode)       |
| <code>[char/letter-or-number?]</code> | `alphaNum`                      | `alpha-num` (unicode)   |                         |
| <code>[char/white?]</code>            | `space`                         | `white-space` (unicode) |                         |
| <code>[char/newline]</code>           | `endOfLine`                     | `new-line*`             |                         |
| <code>[char/str*]</code>              |                                 | `<+>`                   |                         |

[1]: https://github.com/haskell/parsec/blob/master/src/Text/Parsec/Prim.hs

[2]: https://github.com/haskell/parsec/blob/master/src/Text/Parsec/Combinator.hs

[3]: https://github.com/haskell/parsec/blob/master/src/Text/Parsec/Char.hs

[4]: https://github.com/blancas/kern/blob/master/src/main/clojure/blancas/kern/core.clj

[5]: https://github.com/youngnh/parsatron/blob/master/src/clj/the/parsatron.clj

[p/result]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#result

[p/fail]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#fail

[p/fail-unexpected]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#fail-unexpected

[p/expecting]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#expecting

[p/bind]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#bind

[p/for]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#for

[p/after]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#after

[p/value]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#value

[p/maybe]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#maybe

[p/look-ahead]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#look-ahead

[p/not-followed-by]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#not-followed-by

[p/*many]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#*many

[p/+many]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#+many

[p/*skip]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#*skip

[p/+skip]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#+skip

[p/token]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#token

[p/token-not]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#token-not

[p/word]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#word

[p/any-token]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#any-token

[p/eof]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#eof

[p/group]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#group

[p/alt]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#alt

[p/option]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#option

[p/between]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#between

[p/times]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#times

[p/*many-till]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#*many-till

[p/*sep-by]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#*sep-by

[p/+sep-by]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#+sep-by

[p/*sep-end-by]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#*sep-end-by

[p/+sep-end-by]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#+sep-end-by

[p/*sep-opt-by]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#*sep-opt-by

[p/+sep-opt-by]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#+sep-opt-by

[p/get-state]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#get-state

[p/set-state]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#set-state

[p/update-state]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#update-state

[p/trace]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.parser#trace

[expr/*chain-left]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.expr#*chain-left

[expr/+chain-left]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.expr#+chain-left

[expr/*chain-right]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.expr#*chain-right

[expr/+chain-right]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.expr#+chain-right

[char/is]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.char#is

[char/is-not]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.char#is-not

[char/regex]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.char#regex

[char/upper?]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.char#upper?

[char/lower?]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.char#lower?

[char/letter?]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.char#letter?

[char/number?]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.char#number?

[char/letter-or-number?]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.char#letter-or-number?

[char/white?]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.char#white?

[char/newline]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.char#newline

[char/str*]: https://cljdoc.org/d/com.github.strojure/parsesso/CURRENT/api/strojure.parsesso.char#str*

## Examples

* [HoneySQL SELECT](test/demo/honeysql_select.clj)

## Performance

See some benchmarks [here](test/perf/bench.clj).

## Contributors

- [Michiel Borkent](https://github.com/borkdude)
  + Compatibility with babashka.
  + Github CI configuration.
