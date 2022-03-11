package pinacolada.cards.fool.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTrait;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.pcl.Fire;
import pinacolada.powers.common.BurningPower;
import pinacolada.utilities.PCLActions;

public class Elsword extends FoolCard
{
    public static final PCLCardData DATA = Register(Elsword.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetTraits(PCLCardTrait.Protagonist)
            .SetSeriesFromClassPackage();

    public Elsword()
    {
        super(DATA);

        Initialize(8, 5, 2);
        SetUpgrade(2,  1, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Light(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL).forEach(d -> d.SetVFXColor(Color.RED));


        if (m.hasPower(BurningPower.POWER_ID))
        {
            PCLActions.Bottom.Motivate();
        }
        else
        {
            PCLActions.Bottom.ChannelOrb(new Fire());
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Cycle(name, magicNumber);
    }
}