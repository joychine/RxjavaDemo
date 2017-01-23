package com.example.rxjava.rxjavademo;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

public class RxJavaDemo {

	public  void main(String[] args) {
		// testFilter();
		 testCreate();
		// testFilter();
		// testTransform();
		// testOther();
		// syncBatchPut().subscribe();

//		repeatWhen();
//		 retryWhen();
		// heartbeat();
		// while(true){}
		// Observable.error(new IllegalArgumentException("test")).subscribe(new
		// Subscriber<Object>() {
		//
		// @Override
		// public void onCompleted() {
		// // TODO Auto-generated method stub
		// System.out.println("onCompleted");
		// }
		//
		// @Override
		// public void onError(Throwable arg0) {
		// // TODO Auto-generated method stub
		// System.out.println("onError");
		// }
		//
		// @Override
		// public void onNext(Object arg0) {
		// // TODO Auto-generated method stub
		// System.out.println("onNext:" + arg0);
		// }
		// });

		// subject();
		// scan();
		// doOn();
		// scheduler();
		// error();
		// defer();
		// combineLatest();
		// testSub();
//		 intel();
//		
//		Observable.create(new OnSubscribe<Integer>() {
//
//			@Override
//			public void call(Subscriber<? super Integer> arg0) {
//				// TODO Auto-generated method stub
//				arg0.onNext(1);
//			}
//		}).flatMap(new Func1<Integer, Observable<? extends Long>>() {
//
//			@Override
//			public Observable<? extends Long> call(Integer arg0) {
//				// TODO Auto-generated method stub
//				System.out.println("flatMap");
//				return Observable.timer(2000, TimeUnit.MILLISECONDS);
//			}
//		}).map(new Func1<Long, Integer>() {
//
//			@Override
//			public Integer call(Long arg0) {
//				System.out.println("map");
//				return 1;
//			}
//		}).subscribe(new Subscriber<Integer>() {
//
//			@Override
//			public void onCompleted() {
//				System.out.println("onCompleted");
//			}
//
//			@Override
//			public void onError(Throwable arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onNext(Integer arg0) {
//				System.out.println("onNext");
//				
//			}
//		});
		priority();
		while(true){}
	}

	public  int delay = 1000;
	 boolean flag = false;

	public  void priority(){
		Subject mBusSubject = new SerializedSubject<>(PublishSubject.create());
		
		for( int i = 0 ; i<=100 ;i++) {
			mBusSubject.subscribe(new Action1() {
                @Override
                public void call(Object object) {
                    System.out.println(" 收到订阅内容："+object);
                }
            });
		}
		
//		for( int i = 0 ; i<=100 ;i++) {
			mBusSubject.onNext(123);
//		}
	
		
	}
	public  void intel() {
		Observable.interval(0, 1000, TimeUnit.MILLISECONDS).filter(new Func1<Long, Boolean>() {

			@Override
			public Boolean call(Long arg0) {
				System.out.println("----filter....");
				return true;
			}
		}).takeUntil(new Func1<Long, Boolean>() {

			@Override
			public Boolean call(Long arg0) {
				System.out.println("arg0:" + arg0);
				return flag;
			}
		}).filter(new Func1<Long, Boolean>() {

			@Override
			public Boolean call(Long arg0) {
				if (arg0 == 2) {
					flag = true;
				}
				return true;
			}
		}).subscribe(new Action1<Long>() {

			@Override
			public void call(Long arg0) {
				System.out.println("---subscribe---" + arg0);
			}
		});

		while (true) {
		}
	}

	public  void testSub() {

		// Subscription subscription = Observable.error(new
		// IllegalArgumentException("test")).observeOn(Schedulers.io())
		// .subscribe(new Subscriber() {
		//
		// @Override
		// public void onCompleted() {
		//
		// }
		//
		// @Override
		// public void onError(Throwable arg0) {
		// System.out.println("sleep before-------");
		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// System.out.println("sleep after-------");
		// }
		//
		// @Override
		// public void onNext(Object arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		CompositeSubscription mCompositeSubscription = new CompositeSubscription();
		Subscription subscription = Observable.just("").filter(s -> {
			System.out.println("filter " + getThreadInfo());
			long time = System.currentTimeMillis();
			for (long i = 0; i < 10000000000L; i++) {
			}
			long endtime = System.currentTimeMillis();
			System.out.println((endtime - time) + "");
			return true;
		}).subscribeOn(Schedulers.io()).subscribe(checkPutData -> {
			System.out.println("subscribe " + getThreadInfo());
			System.out.println("jump CActiity .");

		}, throwable -> {
			System.out.println("jump CActiity .");
		});

		mCompositeSubscription.add(subscription);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("testsub");
		mCompositeSubscription.unsubscribe();
		mCompositeSubscription.clear();
		System.out.println(subscription);
		System.out.println(mCompositeSubscription.isUnsubscribed());
		while (true) {
		}
	}

	public  String getThreadInfo() {
		return Thread.currentThread().getName() + "  " + Thread.currentThread().getId();
	}

	public  void combineLatest() {
		Observable<Long> intervalObservable = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
				.onErrorResumeNext(Observable.just(null));
		Observable<Long> heartbeatObservable = Observable.create(new OnSubscribe<Long>() {

			@Override
			public void call(Subscriber<? super Long> arg0) {
				arg0.onError(new Throwable("test error"));
				// arg0.onNext(1L);
				// arg0.onCompleted();
			}
		})
				// .onErrorReturn(new Func1<Throwable, Long>() {
				//
				// @Override
				// public Long call(Throwable arg0) {
				// return 77L;
				// }
				// })
				.onErrorResumeNext(new Func1<Throwable, Observable<? extends Long>>() {

					@Override
					public Observable<? extends Long> call(Throwable arg0) {
						// 这里做一些错误处理。
						System.out.println("onErrorResumeNext ....");
						return Observable.just(null);
					}
				});

		Observable.combineLatest(intervalObservable, heartbeatObservable, new Func2<Long, Long, Long>() {

			@Override
			public Long call(Long arg0, Long arg1) {
				if (arg1 != null) {
					System.out.println("not null ....");
				} else {
					System.out.println(" null ....");
				}
				return arg0;
			}
		}).subscribe();

		while (true) {
		}
	}

	public  void defer() {
		Observable.defer(new Func0<Observable<Long>>() {

			@Override
			public Observable<Long> call() {
				return Observable.interval(0, delay, TimeUnit.MILLISECONDS);
			}
		}).subscribe(new Action1<Long>() {
			@Override
			public void call(Long arg0) {
				System.out.println(arg0);
			}
		});

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					delay += 2000;
				}
			}
		}).start();

		while (true) {
		}
	}

	public  void error() {
		Observable.create(new OnSubscribe<String>() {

			@Override
			public void call(Subscriber<? super String> arg0) {
				arg0.onNext("11");
//				RemoteException e = new RemoteException("RemoteException test.");
//				arg0.onError(e);
			}
		}).subscribe(new Action1<String>() {

			@Override
			public void call(String arg0) {
				System.out.println("--Action1--");
			}
		}, new Action1<Throwable>() {

			@Override
			public void call(Throwable arg0) {
				System.out.println("--Throwable---" + arg0.getMessage());
			}
		});
	}

	public  void scheduler() {
		Observable.create(new OnSubscribe<String>() {

			@Override
			public void call(Subscriber<? super String> arg0) {
				System.out.println("create 当前线程名：" + Thread.currentThread().getName() + " --- 当前线程Id:"
						+ Thread.currentThread().getId());
				arg0.onNext("snamon");
				arg0.onNext("licheng is a sha b");
				arg0.onCompleted();
			}
		}).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).doOnTerminate(new Action0() {

			@Override
			public void call() {
				System.out.println("doOnTerminate 当前线程名：" + Thread.currentThread().getName() + " --- 当前线程Id:"
						+ Thread.currentThread().getId());
			}
		})

				.subscribe(new Subscriber<String>() {

					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable arg0) {

					}

					@Override
					public void onNext(String arg0) {
						System.out.println("subscribe 当前线程名：" + Thread.currentThread().getName() + " --- 当前线程Id:"
								+ Thread.currentThread().getId());
					}
				});

		while (true) {
		}

	}

	public  void doOn() {
		// Observable<Integer> observable = Observable.range(1, 50);
		// observable.doOnEach(new Action1<Notification<? super Integer>>() {
		//
		// @Override
		// public void call(Notification<? super Integer> arg0) {
		// System.out.println("doOn doOnEach " + arg0);
		// }
		// }).doOnNext(new Action1<Integer>() {
		//
		// @Override
		// public void call(Integer arg0) {
		// System.out.println("doOn doOnNext " + arg0);
		// }
		// }).subscribe(new Action1<Integer>() {
		//
		// @Override
		// public void call(Integer arg0) {
		// System.out.println("doOn subscribe " + arg0);
		// }
		// }) ;

		Observable<Integer> observable = Observable.just(1);

		observable.map(new Func1<Integer, String>() {

			@Override
			public String call(Integer arg0) {
				System.out.println("doOn map sleep before 当前线程名： " + Thread.currentThread().getName() + " --- 当前线程Id:"
						+ Thread.currentThread().getId());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("doOn map sleep after 当前线程名： " + Thread.currentThread().getName() + " --- 当前线程Id:"
						+ Thread.currentThread().getId());
				return arg0 + "";
			}
		}).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).doOnRequest(new Action1<Long>() {

			@Override
			public void call(Long arg0) {
				System.out.println("doOn doOnRequest 当前线程名： " + Thread.currentThread().getName() + " --- 当前线程Id:"
						+ Thread.currentThread().getId());
			}
		}).doOnTerminate(new Action0() {

			@Override
			public void call() {
				System.out.println("doOn doOnTerminate 当前线程名： " + Thread.currentThread().getName() + " --- 当前线程Id:"
						+ Thread.currentThread().getId());
			}
		}).subscribe(new Action1<String>() {

			@Override
			public void call(String arg0) {
				System.out.println("doOn subscribe 当前线程名： " + Thread.currentThread().getName() + " --- 当前线程Id:"
						+ Thread.currentThread().getId());
			}
		});

		while (true) {
		}

	}

	public  void scan() {
		Observable<Integer> observable = Observable.range(1, 50);

		observable.scan(new Func2<Integer, Integer, Integer>() {

			@Override
			public Integer call(Integer arg0, Integer arg1) {
				return arg0 + arg1;
			}
		}).subscribe(new Action1<Integer>() {

			@Override
			public void call(Integer arg0) {
				System.out.println("scan :" + arg0);
			}
		});

	}

	public  void subject() {

		AsyncSubject<Integer> subject1 = AsyncSubject.create();
		subject1.onNext(2);
		subject1.onNext(3);
		subject1.onNext(4);
		subject1.onCompleted();
		subject1.subscribe(new Action1<Integer>() {

			@Override
			public void call(Integer arg0) {
				System.out.println("AsyncSubject " + arg0);
			}
		});

		subject1.onNext(1);

		BehaviorSubject<Integer> subject = BehaviorSubject.create(-1);
		subject.onNext(2);
		subject.onNext(3);
		subject.onNext(4);
		subject.subscribe(new Action1<Integer>() {

			@Override
			public void call(Integer arg0) {
				System.out.println("behaviorSubject " + arg0);
			}
		});

		subject.onNext(1);

		PublishSubject<Integer> subject2 = PublishSubject.create();
		subject2.onNext(2);
		subject2.onNext(3);
		subject2.onNext(4);
		subject2.subscribe(new Action1<Integer>() {

			@Override
			public void call(Integer arg0) {
				System.out.println("PublishSubject " + arg0);
			}
		});
		//
		subject2.onNext(1);
		subject2.onNext(5);
		subject2.onNext(6);

		ReplaySubject<Integer> subject3 = ReplaySubject.create();
		subject3.onNext(2);
		subject3.onNext(3);
		subject3.onNext(4);
		subject3.subscribe(new Action1<Integer>() {

			@Override
			public void call(Integer arg0) {
				System.out.println("ReplaySubject " + arg0);
			}
		});

		subject3.onNext(1);

		//
		//
		// subject.onNext(2);
		// subject.onNext(3);

	}

	public  void heartbeat() {
		Observable.interval(1000, TimeUnit.MILLISECONDS).flatMap(new Func1<Long, Observable<String>>() {
			@Override
			public Observable<String> call(Long aLong) {
				System.out.println("检测心跳" + aLong);
				if (aLong == 5) {
					return Observable.error(new IllegalArgumentException("error"));
				} else
					return Observable.just("success");
			}
		}).observeOn(Schedulers.io()).doOnSubscribe(new Action0() {
			@Override
			public void call() {
				System.out.println("启动心跳");
			}
		}).subscribe(new Subscriber<String>() {
			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("心跳失败");
			}

			@Override
			public void onNext(String str) {
				System.out.println(str + "心跳成功");
			}
		});

	}

	public  void testOther() {

//		Observable.create(new ScanBarcodeSubscribe()).subscribe(new Action1<String>() {
//
//			@Override
//			public void call(String result) {
//				System.out.println("接收到扫描结果：" + result);
//			}
//		});
//		System.out.println("testOther");

	}

	public  void retryWhen() {

		Observable.create(new OnSubscribe<String>() {

			@Override
			public void call(Subscriber<? super String> arg0) {
				System.out.println("create...." + index);
				throw new NullPointerException("test");
				// for(int i=0 ; i<20 ;i++){
				// arg0.onNext(""+index++);
				// if(index == 3){
				// throw new NullPointerException("test");
				// }
				// }
			}
		}).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {

			@Override
			public Observable<?> call(Observable<? extends Throwable> arg0) {
				System.out.println("retryWhen");
				return Observable.timer(1000, TimeUnit.MILLISECONDS);
//				return arg0.flatMap(new Func1<Throwable, Observable<?>>() {
//					@Override
//					public Observable<?> call(Throwable throwable) {
//						if (throwable instanceof NullPointerException) {
//							System.out.println("异常是 NullPointerException 重试");
//							return Observable.timer(1000, TimeUnit.MILLISECONDS);
//						} else {
//							return Observable.error(throwable);
//						}
//					}
//				});
			}
		}).subscribe(new Subscriber() {

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0) {
				// TODO Auto-generated method stub
				System.out.println("error " + arg0);
			}

			@Override
			public void onNext(Object arg0) {
				// TODO Auto-generated method stub

			}
		});

		while (true) {

		}
	}

	public  void repeatWhen() {

		Observable.create(new OnSubscribe<Integer>() {

			@Override
			public void call(Subscriber<? super Integer> arg0) {
				arg0.onNext(1);
				arg0.onCompleted();
			}
		}).repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
			@Override
			public Observable<?> call(Observable<? extends Void> observable) {
				// 重复3次
				index++;
				System.out.println("repeatWhen");
				return observable.flatMap(new Func1<Void, Observable<?>>() {
					
					@Override
					public Observable<?> call(Void arg0) {
						if(index ==10){
							System.out.println("重订阅");
							return Observable.empty();
						}else{
							System.out.println("重订阅 次数："+ index);
							return Observable.timer(1000, TimeUnit.MILLISECONDS ,Schedulers.immediate());
						}
					}
				});
				
			}
		} , Schedulers.immediate()).subscribe(new Subscriber<Integer>() {
			@Override
			public void onCompleted() {
				System.out.println("Sequence complete.");
			}

			@Override
			public void onError(Throwable e) {
				System.err.println("Error: " + e.getMessage());
			}

			@Override
			public void onNext(Integer value) {
				System.out.println("Next:" + value);
			}
		});

		while (true) {

		}
	}

	 int index = 0;
	/**
	 * 同步后台格口信息
	 */
	// public  Observable<Boolean> syncBatchPut(){
	//
	// return Observable.create(new OnSubscribe<String>(){
	//
	// @Override
	// public void call(Subscriber<? super String> arg0) {
	// System.out.println("create...." + index);
	// arg0.onNext(""+index++);
	// if(index == 3){
	// throw new NullPointerException("test");
	// }
	// }}).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>()
	// {
	//
	// @Override
	// public Observable<?> call(Observable<? extends Throwable> arg0) {
	// arg0.flatMap(new Func1<T, R>() {
	// })
	// return null;
	// }
	// }).doOnNext(new Action1<String>() {
	// @Override
	// public void call(String responseBody) {
	// System.out.println("doOnNext-----" + responseBody);
	// }
	// }).map(new Func1<String, Boolean>() {
	//
	// @Override
	// public Boolean call(String arg0) {
	// System.out.println("map-----" + arg0);
	// return true;
	// }
	// }).subscribeOn(Schedulers.io());
	// }

	/**
	 * 创建一个observable
	 */
	@Test
	public  void testCreate() {
		Observable<Integer> observable1 = Observable.create(new OnSubscribe<Integer>() {

			@Override
			public void call(Subscriber<? super Integer> subscriber) {
				subscriber.onStart();
				for (int i = 0; i < 10; i++) {
					subscriber.onNext(i);
					if (i == 5)
						throw new IllegalArgumentException("number can not 5!!!");
				}
				subscriber.onCompleted();
			}
		});
		String[] array = { "snamon", "sunny", "joychine", "xshine", "ephone" };
		Observable<String> observable2 = Observable.from(array);
		Observable<Long> observable3 = Observable.just(System.currentTimeMillis(), null); // 可以传入null
																							// 并且不会报错
		Observable<String> observable4 = Observable.never(); // 并不会阻塞线程 它马上就执行完了
		Observable<Integer> observable5 = Observable
				.error(new IllegalArgumentException("create a error Observable!!!")); // 这里面接收的泛型无所谓
		Observable<String> observable6 = Observable.empty(); // 直接调用onComplete方法
		Observable<Long> observable7 = Observable.timer(500, TimeUnit.MILLISECONDS); // 这个操作符意思是
																						// 等待500毫秒发送一个0
																						// 的数据
																						// 如果要每隔一段时间发送数据使用timer(long
																						// initialDelay,
																						// long
																						// period,
																						// TimeUnit
																						// unit,
																						// Scheduler
																						// scheduler)
																						// 不过现在已经废弃使用interval操作符
																						// 了。
		Observable<Long> observable8 = Observable.interval(1, TimeUnit.SECONDS); // 每隔一秒发送一次数据
																					// 有四个重载函数
																					// 同样是非阻塞
																					// 我们要看到现象
																					// 要阻塞这个线程
																					// 不然
																					// 它就执行完了
																					// 我们就看不到数据打印数据
		Observable<Integer> observable9 = Observable.range(1, 2);
		Observable<Long> observable10 = Observable.defer(new Func0<Observable<Long>>() {
			@Override
			public Observable<Long> call() {
				return Observable.just(System.currentTimeMillis());
			}
		}); // 表示延时发送 即在订阅的时候才去调用 看第3个和10个打印信息可以看出 obserable在订阅前就把结果已经计算好了
			// 订阅时直接返回了结果。 defer包装下 则可以等待订阅的时候才去调用System.currentTimeMillis

		// 订阅绑定
		bindSubscribe(observable1, "create");
		bindSubscribe(observable2, "from");
		bindSubscribe(observable3, "just");
		bindSubscribe(observable4, "never");
		bindSubscribe(observable5, "error");
		bindSubscribe(observable6, "empty");
		bindSubscribe(observable7, "timer");
		bindSubscribe(observable8, "interval");
		bindSubscribe(observable9, "range");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bindSubscribe(observable3, "just");
		bindSubscribe(observable10, "defer");
		// while(true){}
	}

	/**
	 * rxjava过滤元素
	 */
	public  void testFilter() {

		Observable<Integer> observable = Observable.range(0, 10);
		// filter一般作null验证
		bindSubscribe(observable.filter(new Func1<Integer, Boolean>() {
			@Override
			public Boolean call(Integer t) {
				return t % 5 == 0;
			}
		}), "filter");
		String[] array = { "snamon", "sunny", "joychine", "xshine", "ephone", "sunny" };
		Observable<String> observable2 = Observable.from(array);

		bindSubscribe(observable.take(3), "take");
		bindSubscribe(observable.take(10, TimeUnit.NANOSECONDS), "take");
		bindSubscribe(observable.takeLast(2), "takeLast"); // 获取后面2个
		bindSubscribe(observable.takeLast(1, TimeUnit.NANOSECONDS), "takeLast"); // 获取最后10微秒的数据
		bindSubscribe(observable2.distinct(), "distinct"); // 默认返回的key是它的自己本身
		bindSubscribe(observable2.distinct(new Func1<String, String>() { // 通过源码分析
																			// distinct会根据
																			// 你返回的值作为Key
																			// 然后保存在一个HashSet里面判断是否存在
																			// 。

			@Override
			public String call(String t) {
				return t.substring(0, 1); // 把每一个字符的首字母作为key
			}
		}), "distinct1");

		bindSubscribe(observable2.distinctUntilChanged(), "distinctUntilChanged"); // 相邻二个是不是相等
																					// 跟distinct功能类似
		bindSubscribe(observable2.first(), "first"); // 效果跟 take(1).single() 一样
		bindSubscribe(observable2.first(new Func1<String, Boolean>() { // 根据条件获取第一个
																		// 跟filter(predicate).take(1).single()或者takeFirst(predicate).single()效果是一样
			@Override
			public Boolean call(String t) {
				return true;
			}
		}), "first1"); // 还有1个 first开头 firstOrDefault

		// 相反的 有 last 跟first 反过来 取最后一个
		// skip skipLast跳过n个元素
		// observable.skip(count) 这个源码设计 非常巧妙
		bindSubscribe(observable2.elementAt(1), "elementAt"); // 发送第1个元素
		bindSubscribe(observable2.timeInterval(), "timeInterval"); // 发送的时间
		// bindSubscribe(observable2.all(predicate), tag);
		// bindSubscribe(observable2.timeout(new Func0<Observable<String>>() {
		//
		// @Override
		// public Observable<String> call() {
		// return null;
		// }
		// } , new Func1<String, String>() {
		//
		// @Override
		// public String call(String t) {
		// return null;
		// }
		// }), "timeout");

		Observable observable3 = Observable.just(new TimeoutException("测试超时异常"))
				.retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {

					@Override
					public Observable<?> call(Observable<? extends Throwable> t) {
						System.out.println("aaaaaaaaaa");
						return t.flatMap(new Func1<Throwable, Observable<?>>() {

							@Override
							public Observable<?> call(Throwable t) {
								System.out.println("重试次数：" + (++count));
								return Observable.timer(3000, TimeUnit.MILLISECONDS);
							}
						});
					}
				});

		bindSubscribe(observable3, "retryWhen");

		while (true) {
		}

	}

	 int count = 0;

	public  void testTransform() {
		Observable<Integer> observable = Observable.range(0, 10);
		// map flatmap
		bindSubscribe(observable.map(new Func1<Integer, String>() {

			@Override
			public String call(Integer t) {
				return "map:" + t; // 内部实现也非常地简单 直接在Subscribe调用 call的时候 调用传递过去
									// 的这个Func1
									// 然后调用actual.onNext(result);回调给我们订阅者
			}
		}), "map");

		bindSubscribe(observable.flatMap(new Func1<Integer, Observable<String>>() {

			@Override
			public Observable<String> call(Integer t) {

				return Observable.just("flatMap :" + t);
			}
		}), "flatMap");

		Observable observable2 = observable.merge(Observable.range(1, 3), Observable.just("snamon", "sunny"));
		bindSubscribe(observable2, "merge");

		bindSubscribe(observable.cast(Integer.class), "cast"); // cast是将一个类强转成另一个类
																// 首页这个参数的class一定要是instanceOf
																// 发射出来的元素
		bindSubscribe(observable.buffer(2), "buffer");
		// observable.doOnSubscribe(subscribe)
		// bindSubscribe(observable2.buffer(bufferClosingSelector), tag);
	}

	public  void bindSubscribe(Observable observable, String tag) {
		observable.subscribe(new MySubscribe("Observable " + tag + "操作符"));
	}

	// public  void testFilter(){
	//
	// List<Integer> list = new ArrayList<>() ;
	// for (int i = 0; i < 30; i++) {
	// list.add(i) ;
	// }
	//
	// List<String> list1 = new ArrayList<>() ;
	// for (int i = 0; i < 20; i++) {
	// list1.add(i+"") ;
	// }
	//
	// Observable.concat(Observable.just("snamon","xshine"),
	// Observable.just("sunny"), Observable.just("joychine"))
	// .first(new Func1<String, Boolean>() {
	//
	// @Override
	// public Boolean call(String t) {
	// return t!=null && !t.equals("");
	// }
	// }).subscribe(new Action1<String>() {
	//
	// @Override
	// public void call(String t) {
	// System.out.println(t);
	// }
	// }) ;
	//
	//// Observable.from(list).filter(new Func1<Integer, Boolean>() {
	////
	//// @Override
	//// public Boolean call(Integer num) {
	//// if(num!=0 &&num%4==0)
	//// return true;
	//// else
	//// return false ;
	//// }
	//// }).debounce(5,TimeUnit.MILLISECONDS).
	//// subscribe(new Observer<Integer>() {
	////
	//// @Override
	//// public void onCompleted() {
	//// System.out.println("onCompleted");
	//// }
	////
	//// @Override
	//// public void onError(Throwable e) {
	//// System.out.println("onError");
	//// }
	////
	//// @Override
	//// public void onNext(Integer b) {
	//// System.out.println(b);
	//// }
	//// }) ;
	//
	// }

	final  class MySubscribe<T> extends Subscriber<T> {

		public String tag;

		public MySubscribe(String tag) {
			this.tag = tag;
		}

		@Override
		public void onCompleted() {
			System.out.println(tag + " onCompleted....");
		}

		@Override
		public void onError(Throwable e) {
			System.out.println(tag + " onError : " + e.getMessage());

		}

		@Override
		public void onNext(T t) {
			System.out.println(tag + " onNext :" + t);
		}

	}
}
