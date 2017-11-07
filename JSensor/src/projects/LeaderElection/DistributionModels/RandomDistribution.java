package projects.LeaderElection.DistributionModels;

import jsensor.nodes.Node;
import jsensor.nodes.models.DistributionModelNode;
import jsensor.utils.Configuration;
import jsensor.utils.Position;

public class RandomDistribution extends DistributionModelNode {

	@Override
	public Position getPosition(Node arg0) {
		// TODO Auto-generated method stub
		return new Position(arg0.getRandom().nextInt(Configuration.dimX), arg0.getRandom().nextInt(Configuration.dimY));
	}

}
