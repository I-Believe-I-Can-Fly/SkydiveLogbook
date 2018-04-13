# Project 1 - SkydiveLogbook

## Project participants
| Name                | Study | Studentnr. |
| --------------------|:------:|-----------:|
| [Brede Fritjof Klausen](https://github.com/BredeFK) | BPROG  | xxxxxx |
| [Johan Aanesen](https://github.com/johanAanesen/)        | BPROG | xxxxxx |
| [Jørgen Jærnes](https://github.com/jorgenjj)      | BPROG | xxxxxx |

## Style
When something is written to the user, it's from resource, so it's easier to change text and/or translate.
### How
#### Adding string resource
[Change the UI strings](https://developer.android.com/training/basics/firstapp/building-ui.html)

### Retrieving string resource
```java
  TextView txtHello = findViewById(R.id.hello_ID);
  // Retrieving the string with id 'hello'  
  String helloText = this.getResources().getString(R.string.hello);
  txtHello.setText(helloText);
```

## Sources
-
