package pinacolada.cards.fool.basic;

import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.resources.PGR;

public abstract class ImprovedBasicCard extends FoolCard
{
    public final PCLAffinity affinity;

    public ImprovedBasicCard(PCLCardData data, PCLAffinity affinity, String foregroundTexturePath)
    {
        super(data);

        InitializeAffinity(affinity, 1, 0, 1);

        this.affinity = affinity;
        this.cropPortrait = false;
        this.portraitImg.color = affinity.GetAlternateColor(0.85f);
        this.portraitForeground = new AdvancedTexture(PGR.GetTexture(foregroundTexturePath, true), null);

        SetTag(PGR.Enums.CardTags.IMPROVED_BASIC_CARD, true);
    }
}