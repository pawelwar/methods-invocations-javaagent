# Log methods invocations by javaagent (example)

Project contains two modules. First is a javaagent `invocationsagent` responsible for logging method invocations. Second module is a simple java application `someapplication` which do a few methods invocations. It will be enhanced by javaagent instructions.

### Run

Just: `./gradlew run`. 

It automatically injects `invocationsagent` as `-javaagent:...` to `someapplication`.

### Output

```
[19:12:58.714] load: warek.application.AppMain
[19:12:58.811] enter: main
[19:12:58.816] load: warek.application.AppMain.TestService
[19:12:58.826] enter: warek.application.AppMain$TestService
[19:12:58.833] exit: warek.application.AppMain$TestService
[19:12:58.833] enter: someLongRunningMethod
[19:12:58.833] enter: randomSleep
[19:13:00.165] exit: randomSleep
[19:13:00.165] exit: someLongRunningMethod
[19:13:00.166] enter: someMethod
[19:13:00.166] exit: someMethod
[19:13:00.166] enter: warek.application.AppMain$TestService
[19:13:00.166] exit: warek.application.AppMain$TestService
[19:13:00.167] enter: someLongRunningMethod
[19:13:00.167] enter: randomSleep
[19:13:04.919] exit: randomSleep
[19:13:04.920] exit: someLongRunningMethod
[19:13:04.920] enter: someMethod
[19:13:04.920] exit: someMethod
[19:13:04.920] exit: main
```     

### Output interpretation

1. **Main** class is loaded by loader

```
[19:12:58.714] load: warek.application.AppMain
```

2. Static method **main** is invoked

```
[19:12:58.811] enter: main
```

3. **TestService** is loaded and constructed

```
[19:12:58.816] load: warek.application.AppMain.TestService
[19:12:58.826] enter: warek.application.AppMain$TestService
[19:12:58.833] exit: warek.application.AppMain$TestService
```

4. First method **someLongRunningMethod** is invoked. Inside this method, the next one is called **randomSleep**. 

```
[19:12:58.833] enter: someLongRunningMethod
[19:12:58.833] enter: randomSleep
[19:13:00.165] exit: randomSleep
[19:13:00.165] exit: someLongRunningMethod
(...)
```
