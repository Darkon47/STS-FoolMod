package pinacolada.cards.fool.series.OwariNoSeraph;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTrait;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.Yuuichirou_Asuramaru;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Yuuichirou extends FoolCard
{
    public static final PCLCardData DATA = Register(Yuuichirou.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .SetTraits(PCLCardTrait.Protagonist)
            .PostInitialize(data -> data
                    .AddPreview(new Yuuichirou_Asuramaru(), true));

    public Yuuichirou()
    {
        super(DATA);

        Initialize(7, 0, 2);
        SetUpgrade(3, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        PCLActions.Bottom.Draw(1).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                if (PCLGameUtilities.IsHindrance(c)) {
                    PCLActions.Top.Draw(1);
                }
            }
        });
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.MakeCardInDiscardPile(new Yuuichirou_Asuramaru()).SetUpgrade(upgraded, false);
        }
    }
}