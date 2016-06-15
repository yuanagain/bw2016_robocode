
gitadd:
	git add ./*.java
	git add ./*/*.java
	git add Makefile
	
clean:
	rm -rf *.class *~

countlines:
	find ./ -name '*.java' | xargs wc -l


quickpush:
	git add ./*.java
	git add Makefile
	git add ./*/*.java
	git commit -m 'quickpush'
	git push
