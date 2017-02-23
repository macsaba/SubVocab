# SubVocab

SubVocabv1.0.jar is the executable file.

This application uses the UCanAccess libraries for SQL queries. Download it and add before build the code.
http://ucanaccess.sourceforge.net/site.html

#How does it work?
This application helps you watch films in foreign languages with subtitles. It searches for the unknown terms in the
text (the known words has a database), and asks you to make a translation for them. Then it inserts the meaning of the
terms into the subtitles (creates a new .srt file), making easier understanding the text.

For example:
Your db contains the following words as a known word: je, suis, ton, etc. 
Subtitle: "Je suis ton pére!", which means "I am your father!".
The application will find the word "pére", and asks you to make a definition. You set the definition to "father".
The new subtitle is: "Je suis ton pére(= father)!"

The application filters the plurals, and some other suffixes, so you don't have to define father and fathers too.
This function only works with English words.
