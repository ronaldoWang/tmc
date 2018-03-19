package cn.droidlover.xdroidmvp.test.testmain;

import junit.framework.TestCase;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by ronaldo on 2017/4/20.
 */

public class Rxtest extends TestCase {
    List<Integer> list = new ArrayList<Integer>();

    protected void setUp() {
        list.add(1);
        list.add(2);
        list.add(3);
    }

    /**
     * 简单的处理list
     */
    public void test1() {
        Flowable.just(list).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                for (int i = 0; i < integers.size(); i++) {
                    Integer j = integers.get(i);
                    System.out.println(j);
                }
            }
        });
    }

    /**
     * 使用fromIterable 处理集合对象
     */
    public void test2() {
        Flowable.fromIterable(list).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    /**
     * 使用map处理中间的过程
     */
    public void test3() {
        Flowable.just(list).map(new Function<List<Integer>, List<Integer>>() {
            @Override
            public List<Integer> apply(List<Integer> list) throws Exception {
                for (int i = 0; i < list.size(); i++) {
                    Integer i_ = list.get(i);
                    i_ = i_ + 1;
                    list.set(i, i_);
                }
                return list;
            }
        }).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                for (int i = 0; i < integers.size(); i++) {
                    System.out.println(integers.get(i));
                }
            }
        });
    }

    /**
     * Flowable.flatMap 可以把一个 Flowable 转换成另一个 Flowabl
     */
    public void test4() {
        Flowable.just(list).flatMap(new Function<List<Integer>, Publisher<Integer>>() {
            @Override
            public Publisher<Integer> apply(List<Integer> integers) throws Exception {
                return Flowable.fromIterable(integers);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    /**
     * 使用filter进行过来，返回false为拦截
     */
    public void test5() {
        Flowable.fromIterable(list).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer >= 2;//返回false为拦截
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    /**
     * 使用take设置只需要两个数据
     */
    public void test6() {
        Flowable.fromIterable(list).take(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    /**
     * doOnNext 表示在消费之前执行一些操作
     */
    public void test7() {
        Flowable.fromIterable(list).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("保存:" + integer);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    /**
     * Subscriber  消费者的使用，包含了四个处理的方法
     */
    public void test8() {
        Flowable.fromIterable(list).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);//发起消费请求
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext:" + integer);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("结束调用");
            }
        });
    }

    public void test9(){
        Date date = new Date();
        System.out.println(date.getYear()+1900);
    }

}
