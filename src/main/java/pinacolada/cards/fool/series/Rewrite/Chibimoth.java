package pinacolada.cards.fool.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.CompositeCardEffect;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;

public class Chibimoth extends FoolCard
{
    public static final PCLCardData DATA = Register(Chibimoth.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetTraits(PCLCardTrait.Beast)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new KotoriKanbe(), false));

    public Chibimoth()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0,0,2,0);
        SetAffinity_Star(1, 0, 0);
        SetLoyal(true);
        SetExhaust(true);
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        SetupChoices(true,
                new CompositeCardEffect(
                        GenericCardEffect.GainAffinityPower(this, GenericCardEffect.CardValueSource.MagicNumber, PCLAffinity.Green),
                        GenericCardEffect.Gain(this, GenericCardEffect.CardValueSource.SecondaryNumber, PCLPowerHelper.SupportDamage)
                ),
                new CompositeCardEffect(
                        GenericCardEffect.GainAffinityPower(this, GenericCardEffect.CardValueSource.MagicNumber, PCLAffinity.Orange),
                        GenericCardEffect.Gain(this, GenericCardEffect.CardValueSource.SecondaryNumber, PCLPowerHelper.Thorns)
                )
        ).Select(1, m);

        PCLActions.Bottom.FetchFromPile(name, 1, player.drawPile, player.discardPile)
                        .SetOptions(true, true)
                                .SetFilter(c -> KotoriKanbe.DATA.ID.equals(c.cardID));
    }
}