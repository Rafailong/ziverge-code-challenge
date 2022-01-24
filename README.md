# Ziverge Code Challenge

## Description

In `blackbox` directory you will find binaries for Mac/Linux -
these binaries will print out JSON lines with attributes:
`event_type`, `data`, and `timestamp`.

`event_type` and `data` are strings, `timestamp` are seconds
since the Epoch and increase monotonically.

Your task:

- Create an application that reads the lines emitted by the process and
  performs a windowed (arbitrary duration left for you to choose) word
  count, grouped by `event_type`.
- The window should be measured from the latest observed event timestamp.
- The word count should be the frequency per word, per event type.
- The current word count should be exposed over an HTTP interface from
  your application.
- Note that the binaries sometimes output garbage data, so you’ll need
  to handle that gracefully. 
- The application should be written in Scala with your
  frameworks/libraries of choice (no need to learn something new for
  this challenge - use what you know).
- Create a GitHub repo with a solution and share it with us. It can be
  public but don’t upload the binaries to the repo.
- This is for getting a feeling for your code style and problem-solving.
  Please write code that you are proud of, but keep the
  effort reasonable. We do not need a full production system.

## Solution

### Libraries

- to execute the binary [prox](https://vigoo.github.io/prox/docs/zstream/index.html) seems like a good option

## Questions

- has `event_type` a fix number of values i.e. `foo`, `bar`, and `baz` ?
  It has a limited number of values, but the possible values are unknown.

- should the word count start over for every window (reset the count every window) ?
  That depends on the kind of window you choose, sliding (continuously evict and add events)
  or tumbling (start over).

- will `data` always contain what seems to be a word OR 
  it might contain bigger texts i.e. sentences or paragraphs?
  It will always be a word.
