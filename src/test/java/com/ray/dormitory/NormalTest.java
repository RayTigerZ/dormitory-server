package com.ray.dormitory;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2020.01.13 16:42
 */
public class NormalTest {
    @Test
    public void test() {
        List<As> list = new ArrayList<>();
        list.add(new As(1));
        list.add(new As(2));
        list.add(new As(3));
        new Open<As>(list).out();
    }
}

interface Foo {
    int getScore();
}

class As implements Foo {
    private int sc;

    public As(int s) {
        sc = s;
    }

    @Override
    public int getScore() {
        return sc;
    }
}

class Open<T extends Foo> {
    List<T> list;

    public Open(List<T> list) {
        this.list = list;
    }

    public void out() {
        list.forEach(a -> System.out.println(a.getScore()));
    }
}
