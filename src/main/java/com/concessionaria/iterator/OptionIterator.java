package com.concessionaria.iterator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.concessionaria.model.OptionBundle;
import com.concessionaria.model.OptionComponent;

public class OptionIterator implements Iterator<OptionComponent> {

    private final Deque<Iterator<OptionComponent>> stack = new ArrayDeque<>();
    private OptionComponent next;

    public OptionIterator(OptionBundle root) {
        stack.push(root.getChildren().iterator());
        advance();
    }

    private void advance() {
        next = null;

        while (!stack.isEmpty()) {
            Iterator<OptionComponent> it = stack.peek();
            if (it.hasNext()) {
                OptionComponent current = it.next();
                next = current;

                if (current instanceof OptionBundle) {
                    OptionBundle bundle = (OptionBundle) current;
                    stack.push(bundle.getChildren().iterator());
                }
                return;
            } else {
                stack.pop();
            }
        }
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public OptionComponent next() {
        if (next == null) {
            throw new NoSuchElementException();
        }
        OptionComponent result = next;
        advance();
        return result;
    }
}
