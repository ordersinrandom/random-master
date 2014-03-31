package com.jbp.randommaster.draft.lambda;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang.builder.ToStringBuilder;

public class TestLambda1 {

	static class Person {
		public enum Sex {
			MALE, FEMALE
		}

		private String name;
		private int age;
		private Sex sex;

		public String getName() {
			return name;
		}

		public int getAge() {
			return age;
		}

		public Sex getSex() {
			return sex;
		}

		public Person(String name, int age, Sex s) {
			this.name = name;
			this.age = age;
			this.sex = s;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("name", name).append("age", age).append("sex", sex).toString();
		}
	}

	public static void main(String[] args) {

		List<Person> list1 = new LinkedList<>();
		list1.add(new Person("Paul", 20, Person.Sex.MALE));
		list1.add(new Person("Jodie", 19, Person.Sex.FEMALE));
		list1.add(new Person("Brenton", 5, Person.Sex.MALE));

		processPerson(list1, p -> p.getAge() > 18, p -> p.getAge(), age -> {
			System.out.println("Found a person... processing");
			System.out.println(age);
			System.out.println("Person processed");
		});

	}

	public static void processPerson(Collection<Person> col, Predicate<Person> tester, Function<Person, Integer> mapper, Consumer<Integer> consumer) {

		for (Person p : col) {
			if (tester.test(p)) {
				int i = mapper.apply(p);
				consumer.accept(i);
			}
		}

	}

}
