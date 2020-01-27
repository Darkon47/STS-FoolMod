package eatyourbeets.cards.base.attributes;

import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.RenderHelpers;

public class BlockAttribute extends AbstractAttribute
{
    public static final BlockAttribute Instance = new BlockAttribute();

    @Override
    public AbstractAttribute SetCard(EYBCard card)
    {
        icon = GR.Common.Images.Block.Texture();
        iconTag = null;
        suffix = null;

        mainText = RenderHelpers.GetBlockString(card);

        return this;
    }
}
