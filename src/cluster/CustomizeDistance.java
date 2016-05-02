package cluster;

import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.neighboursearch.PerformanceStats;

public class CustomizeDistance extends EuclideanDistance {

	@Override
	public double distance(Instance first, Instance second, double cutOffValue,
			PerformanceStats stats) {
		double distance = 0;
		int firstI, secondI;
		int firstNumValues = first.numValues();
		int secondNumValues = second.numValues();
		int numAttributes = m_Data.numAttributes();
		int classIndex = m_Data.classIndex();

		validate();

		for (int p1 = 0, p2 = 0; p1 < firstNumValues-1
				|| p2 < secondNumValues-1;) {
			if (p1 >= firstNumValues)
				firstI = numAttributes;
			else
				firstI = first.index(p1);

			if (p2 >= secondNumValues)
				secondI = numAttributes;
			else
				secondI = second.index(p2);

			if (firstI == classIndex) {
				p1++;
				continue;
			}
			if ((firstI < numAttributes) && !m_ActiveIndices[firstI]) {
				p1++;
				continue;
			}

			if (secondI == classIndex) {
				p2++;
				continue;
			}
			if ((secondI < numAttributes) && !m_ActiveIndices[secondI]) {
				p2++;
				continue;
			}

			double diff;

			if (firstI == secondI) {
				diff = difference(firstI, first.valueSparse(p1),
						second.valueSparse(p2));
				p1++;
				p2++;
			} else if (firstI > secondI) {
				diff = difference(secondI, 0, second.valueSparse(p2));
				p2++;
			} else {
				diff = difference(firstI, first.valueSparse(p1), 0);
				p1++;
			}
			if (stats != null)
				stats.incrCoordCount();

			distance = updateDistance(distance, diff);
			if (distance > cutOffValue)
				return Double.POSITIVE_INFINITY;
		}
		return distance;
	}
}
