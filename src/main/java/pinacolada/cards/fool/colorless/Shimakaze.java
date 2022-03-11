package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Shimakaze extends FoolCard
{
    public static final PCLCardData DATA = Register(Shimakaze.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetColorless(PGR.Enums.Cards.THE_FOOL)
            .SetSeries(CardSeries.Kancolle);

    public Shimakaze()
    {
        super(DATA);

        Initialize(2, 2, 3, 2);
        
        SetAffinity_Green(1);
        SetAffinity_Silver(1);
        SetAffinity_Star(0,0,1);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);

        PCLActions.Bottom.AddAffinity(PCLJUtils.Random(PCLAffinity.Extended()), secondaryValue);
        PCLActions.Bottom.Draw(magicNumber);
        PCLActions.Bottom.MakeCardInDrawPile(new Dazed());
    }
}