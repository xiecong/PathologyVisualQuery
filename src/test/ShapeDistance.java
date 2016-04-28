package test;

import java.util.Enumeration;

import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.neighboursearch.PerformanceStats;

public class ShapeDistance implements DistanceFunction{

	@Override
	public String[] getOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<Option> listOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOptions(String[] arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double distance(Instance arg0, Instance arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double distance(Instance arg0, Instance arg1, PerformanceStats arg2)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double distance(Instance arg0, Instance arg1, double arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double distance(Instance arg0, Instance arg1, double arg2,
			PerformanceStats arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAttributeIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instances getInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getInvertSelection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postProcessDistances(double[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttributeIndices(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInstances(Instances arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInvertSelection(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Instance arg0) {
		// TODO Auto-generated method stub
		
	}

}
