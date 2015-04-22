copy /Y backend\build\exploded-app\WEB-INF\appengine-generated\local_db.bin local_db.bin &^
gradlew backend:build &^
mkdir backend\build\exploded-app\WEB-INF\appengine-generated &^
copy /Y local_db.bin backend\build\exploded-app\WEB-INF\appengine-generated\local_db.bin &^
gradlew backend:appengineRun &^
copy /Y backend\build\exploded-app\WEB-INF\appengine-generated\local_db.bin local_db.bin
