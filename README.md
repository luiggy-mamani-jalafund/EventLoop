# Event Loop

| Members                     |
| :-------------------------- |
| Luiggy Mamani Condori       |
| Axel Ayala Siles            |
| Salet Yasmin Gutierrez Nava |

---
# Software Development Management
We used Trello tool to track our progress development for this project, here is the link where you can see our dashboard Trello:

- [EVENT LOOP TEAM - TRELLO DASHBOARD](https://trello.com/invite/b/674286a3f2d9e8a37c2c1434/ATTIb38abf5b46babfbb9a6b00ed147d7769F1F8F1EB/programacion-asincrona)


# Event Loop Integration Script Documentation

The script automates the process of compiling a Java project, creating a JAR file, and copying it to the **Client** project, which uses Gradle. Once the JAR is copied, the script will trigger a Gradle build in the **Client** project to verify the integration. The update to the **Client** project's `build.gradle` to include the JAR as a dependency must be done manually.

## Running the Script

1. Ensure the script has the permissions for execution:
    ```bash
    chmod +x integrate_event_loop.sh
    ```
2. To execute the script, run the following command:
    ```bash
    ./integrate_event_loop.sh
    ```

**Expected output**

```bash
➜ ./integrate_event_loop.sh
12/07/24 09:43:37 | COMPILING EVENT LOOP...
12/07/24 09:43:38 | CREATING EVENT LOOP JAR...
Process logs...
12/07/24 09:43:39 | ADDING EVENT LOOP JAR TO THE CLIENT...
12/07/24 09:43:39 | BUILDING CLIENT WITH GRADLE...
Process logs...
12/07/24 09:43:47 | INTEGRATION COMPLETED SUCCESSFULLY
```

# Examples

## Fibonacci
In this example we will use the event loop to handle the creation of the UI and the change of the incrementing and decrementing number input `as Immediate tasks that will go to the Call Stack`, and the Fibonacci calculation will be `handled as a promise` as an input and output operation, where the number will be the input and the `result will be the output that will be handled with callbacks` to display it in the UI.

**As seen in the example, the increment operations (Call Stack Tasks) are not interrupted while the Fibonacci calculation is being done (Promise that will be sent to the MicroTask Queue).**
![fibonacci_image](./Client/src/main/resources/fibonacci_event_loop.gif)

## Circle Pop Game
In this example, we use the event loop to manage both UI rendering and game logic. The appearance of balloons is handled as `recurring interval tasks` managed by the Timer Queue, while interactions like popping balloons and updating the score are `immediate tasks` sent to the Call Stack.

As balloons are clicked, `the event loop ensures immediate UI updates` (removal of balloons and score increment). Additionally, dynamic speed adjustments for balloon spawning are achieved by modifying the interval timing directly, showcasing `the flexibility of interval-based scheduling.`

**As seen in the example, the Call Stack processes immediate tasks (like balloon pops) without blocking the spawning of new balloons (Timer Queue), maintaining a smooth gameplay experience.**
![circle_pop_game_image](./Client/src/main/resources/circle_pop_game.gif)

## Fast Reaction Game
In this example, we use the EventLoop to handle both the creation of the user interface (UI) and time-related operations and user interactions. This game simulates a timer with a random delay and measures the user's reaction time by clicking a button when it changes color.

### Components of the EventLoop in the Game
**1. Creating the User Interface (Call Stack Tasks)**

Creating and configuring the graphical interface, such as labels, button, and messages, are handled as immediate tasks (ImmediateTask) that are placed on the execution stack (Call Stack). This ensures that the visual elements are rendered and updated sequentially and without interruptions.

Example of a UI creation task:
![Call Stack Tasks](./Client/src/main/resources/CallStack.jpeg)

**2. Timer with `setTimeout` (Timer Queue)**

The button's color change and activation after a random delay are handled by `setTimeout`. This method allows you to schedule a task in the Timer Queue that will be executed after the specified time has elapsed.

Example of using setTimeout:

![setTimeout](./Client/src/main/resources/setTimeout.png)

Here:

- **delay:** This is a random value between `2,000` and `5,000` ms, which makes the game unpredictable.
- The task is placed in the timer queue and executed after the specified delay.

**3. User Interaction (Immediate Tasks)**

When the user clicks the button, the event is handled as an immediate task. This ensures that the logic for calculating the reaction time is processed immediately and not interrupted by other operations.

Example of a user click task:
![ImmediateTasks](./Client/src/main/resources/ImmediateTasks.png)

**4. Automatic Game Restart (Timer Queue)**

After each attempt, the game is automatically restarted using another scheduled task with setTimeout. This allows the user to try again without having to manually restart the game.

Example of automatic restart:
```
startGame();
```

Example of the game:

![FastReactionGame](./Client/src/main/resources/FastReactionGame.gif)
