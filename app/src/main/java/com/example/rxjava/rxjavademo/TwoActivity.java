package com.example.rxjava.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author joychine on 2017/1/23. 15 56
 * @email joychine@qq.com
 */

public class TwoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Student[] students = {};
        Subscriber<Student.Course> subscriber = new Subscriber<Student.Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Student.Course course) {
                Log.d("", course.course_name+"");
            }
        };
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Student.Course>>() {
                    @Override
                    public Observable<Student.Course> call(Student student) {
//                        return Observable.from(student.getCourses());
                        return null;
                    }
                })
                .subscribe(subscriber);

    }
}
