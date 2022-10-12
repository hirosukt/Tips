# Tips

## Configuration
```
containers:
  default:
    seconds: 0
    minutes: 5
    hours: 0
    randomMessage: true
    messages:
      - "Hello everyone!"
      - "This is example tips message."
  hello:
    seconds: 0
    minutes: 0
    hours: 24
    randomMessage: false
    messages:
      - "A day finished."
```

* `seconds`  
seconds of timer.

* `minutes`  
minutes of timer.

* `hours`  
hours of timer.

* `randomMessage`  
randomized message choice from `messages`.

* `messages`
message list of sending.