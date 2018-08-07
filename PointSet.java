//Jake Vitale, Initial Submission, Project 1

import java.util.*;
import java.awt.geom.Point2D;

public class PointSet
{
	public static PointPair closestPair(Point2D.Double[] pts)
	{
		ArrayList<Point2D.Double> x_points = new ArrayList<Point2D.Double>(pts.length);
		ArrayList<Point2D.Double> y_points = new ArrayList<Point2D.Double>(pts.length);

		//O(n)
		for (Point2D.Double p : pts)
		{
			x_points.add(p);
			y_points.add(p);
		}

		//The following 2 Comparators were created with the help of stackoverflow and online java documentation:
		//http://stackoverflow.com/questions/14154127/collections-sortlistt-comparator-super-t-method-example
		//https://www.tutorialspoint.com/java/lang/double_compare.htm
		
		//O(nlog(n))
		Collections.sort(x_points, new Comparator<Point2D.Double>() {
			public int compare(Point2D.Double p1, Point2D.Double p2) {
				if (p1.x != p2.x) return Double.compare(p1.x, p2.x);
				else return Double.compare(p1.y, p2.y);
			}
		});
		//O(nlog(n))
		Collections.sort(y_points, new Comparator<Point2D.Double>() {
			public int compare(Point2D.Double p1, Point2D.Double p2) {
				if (p1.y != p2.y) return Double.compare(p1.y, p2.y);
				else return Double.compare(p1.x, p2.x);
			}
		});
		return closestPair(x_points, y_points);
	}

	public static PointPair closestPair(ArrayList<Point2D.Double> x_points, ArrayList<Point2D.Double> y_points)
	{
		int x_size = x_points.size();
		int y_size = y_points.size();
		if (x_size!=y_size)
		{
			System.out.println("Incorrect sizes of arrays");
			return null;
		}
		else if (x_size <= 3)
		{
			return bruteForce(x_points);
		}
		else
		{
			ArrayList<Point2D.Double> x_points_left = new ArrayList<Point2D.Double>(x_size/2);
			ArrayList<Point2D.Double> x_points_right = new ArrayList<Point2D.Double>(x_size/2);
			ArrayList<Point2D.Double> y_points_left = new ArrayList<Point2D.Double>(x_size/2);
			ArrayList<Point2D.Double> y_points_right = new ArrayList<Point2D.Double>(x_size/2);

			//created to make the creating the division of y_points linear
			HashSet<Point2D.Double> x_points_left_hash = new HashSet<Point2D.Double>();

			//O(n)
			for (int i = 0; i<x_size; i++)
			{
				if (i<x_size/2)
				{
					x_points_left.add(x_points.get(i));
					x_points_left_hash.add(x_points.get(i));
				}
				else
				{
					x_points_right.add(x_points.get(i));
				}
			}

			//This is O(n) because I am calling a contains on a hashtable, not an arraylist
			for (Point2D.Double p : y_points)
			{
				if (x_points_left_hash.contains(p)) y_points_left.add(p);
				else y_points_right.add(p);
			}

			PointPair left = closestPair(x_points_left, y_points_left);
			PointPair right = closestPair(x_points_right, y_points_right);

			PointPair closer = left;
			if (right.getDistance()<left.getDistance()) closer = right;

			//O(1) because I am calling a look up on an indexed ArrayList
			double div = (x_points_left.get(x_points_left.size()-1).x + x_points_right.get(0).x)/2;

			double left_bound = div-closer.getDistance();
			double right_bound = div+closer.getDistance();

			ArrayList<Point2D.Double> my = new ArrayList<Point2D.Double>();

			//O(n)
			for (Point2D.Double p : y_points)
			{
				if (p.x >= left_bound && p.x <= right_bound) my.add(p);
			}

			int my_size = my.size();
			//O(n)
			for (int i = 0; i<my_size; i++)
			{
				//O(1)
				Point2D.Double p = my.get(i);
				double lower_bound = p.y+closer.getDistance();
				//only iterate through the next 7 points
				for (int j = i+1; j<i+8; j++)
				{
					if (j>=my_size) break;
					else
					{
						Point2D.Double other_point = my.get(j);
						if (other_point.y>lower_bound) break;
						else
						{
							PointPair new_pair = new PointPair(p, other_point);
							if (new_pair.getDistance()<closer.getDistance()) closer = new_pair;
						}
					}
				}
			}
			return closer;
		}
	}

	public static PointPair bruteForce(ArrayList<Point2D.Double> x_points)
	{
		if (x_points.size()==2)
		{
			return new PointPair(x_points.get(0), x_points.get(1)).normalize();
		}
		else
		{
			PointPair closest = new PointPair(x_points.get(0), x_points.get(1));
			double closest_dist = closest.getDistance();

			PointPair one_two = new PointPair(x_points.get(1), x_points.get(2));
			double one_two_dist = one_two.getDistance();
			if (one_two_dist<closest_dist)
			{
				closest = one_two;
				closest_dist = one_two_dist;
			}
			PointPair zero_two = new PointPair(x_points.get(0), x_points.get(2));
			double zero_two_dist = zero_two.getDistance();
			if (zero_two_dist<closest_dist)
			{
				closest = zero_two;
				closest_dist = zero_two_dist;
			}
			return closest.normalize();
		}
	}
}





















