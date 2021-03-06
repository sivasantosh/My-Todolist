# My Todolist
This is a todolist app built for android platform using java. This app has multiple activities, RecyclerView, Customized Action Bar, Context Action Bar, and SQLite.

## Implementation Details
### Multiple Activities
This app has 3 activities.

* Main Activity
* New Todo Activity
* Edit Todo Activity

Communication between activities is done through Intents.

### RecyclerView
The todolist is shown using RecyclerView widget. RecyclerView is used instead of ListView because it's better. It has insert/delete/update animations. Views can be updated one item at a time. And of course it's easier than implementing a ListView once you understand the ViewHolder pattern.

### Contextual Action Bar
When a long click event occurs on a todolist item (or click on icon) the contextual action bar will be shown. You can see a checkbox added to each list item. You can mark your todo items as important or completed. You can also do item selection and deletion.

### SQLite
The app uses SQLite database for permanent storage. Instead of writing to database everytime the todolist is changed; the todolist is loaded only once from the database when the app is started. This todolist is buffered in the custom Application class. The application class allows the app to retain the todolist during configuration changes (Note: configuration change causes the activities to be destroyed along with it's data).

The todolist is saved to database everytime app is stopped. Database transactions are used for faster processing and performed using AsyncTask so it doesn't block the main thread.

## SNAPSHOTS

When app starts for first time.

![When app starts for first time](images/app_start.png)


new todo activity (edit todo is similar)

![new todo activity (edit todo is similar)](images/new_todo.png)


the top bar (with icons) is a contextual action bar

![contextual action bar](images/contextual_action_bar.png)


contextual action bar hidden menu

![contextual action bar hidden menu](images/contextual_action_bar_2.png)


todo's marked important and completed

![todo's marked important and completed](images/todo_marks.png)


in landscape mode

![in landscape mode](images/landscape.png)


## TODO

* search - implemented.
* drag and drop - implemented. (swipe also implemented)

Use drag/drop feature to move entries around. Click and hold on an entry to enable drag/drop. Even though it will take you selection mode you can still do drag/drop with the item you are holding.

Use swipe to quick delete an entry.

## Try it

If you want to try it out you can download the [apk file here](https://drive.google.com/file/d/0B-w-mTjuqUzuU2tPLWhjajdZZlE/view?usp=sharing) (1.18MB).