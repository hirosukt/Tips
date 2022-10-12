# Tips
Broadcast tips message per specified time.

## Configuration
```
containers:
  container1:
    seconds: 0
    minutes: 5
    hours: 0
    randomMessage: true
    messages:
      - "Hello everyone!"
      - "This is example tips message."
  container2:
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

## Permissions

* `tips.*`  
receive all tips.

* `tips.<container_name>`  
receive container's tips.
