package pinacolada.cards.fool.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.HPAttribute;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.MadokaKaname_Krimheild;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class MadokaKaname extends FoolCard
{
    public static final PCLCardData DATA = Register(MadokaKaname.class)
            .SetSkill(2, CardRarity.RARE, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .SetTraits(PCLCardTrait.Protagonist, PCLCardTrait.Spellcaster)
            .PostInitialize(data ->
            {
                data.AddPreview(new MadokaKaname_Krimheild(), true);
            });

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 1, 3, 3);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1, 0, 1);

        SetHealing(true);
        SetPurge(true);

        SetSoul(2, 0, MadokaKaname_Krimheild::new);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return secondaryValue <= 0 ? null : HPAttribute.Instance.SetCard(this, false).SetText(new ColoredString(secondaryValue, Colors.Cream(1f)));
    }

    @Override
    public int GetXValue() {
        return secondaryValue * (PCLJUtils.Count(player.drawPile.group, c -> c.type == CardType.CURSE) +
                PCLJUtils.Count(player.discardPile.group, c -> c.type == CardType.CURSE) +
                PCLJUtils.Count(player.hand.group, c -> c.type == CardType.CURSE) +
                PCLJUtils.Count(player.exhaustPile.group, c -> c.type == CardType.CURSE));
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.AddAffinity(PCLAffinity.Light, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        PCLActions.Bottom.PurgeFromPile(name, magicNumber, p.exhaustPile, player.hand, player.discardPile, player.drawPile)
        .ShowEffect(false, false)
        .SetOptions(true, true)
        .SetFilter(c -> CardType.CURSE.equals(c.type))
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                PCLActions.Bottom.Heal(secondaryValue * cards.size());
                PCLActions.Bottom.VFX(new BorderFlashEffect(Color.PINK, true));
            }
        });

        cooldown.ProgressCooldownAndTrigger(m);
    }
}